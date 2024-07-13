package com.kmhoon.web.config.socket;

import com.kmhoon.web.exception.AuctionApiException;
import com.kmhoon.web.exception.code.security.SecurityErrorCode;
import com.kmhoon.web.security.jwt.JwtTokenProvider;
import com.kmhoon.web.service.auction.AuctionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Component
public class SocketChannelInterceptor implements ChannelInterceptor {

    private static final Pattern PARTICIPANT = Pattern.compile("/sub/auction/(\\d+)/participant");

    private final JwtTokenProvider jwtTokenProvider;
    private final AuctionService auctionService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if(accessor.getCommand() == StompCommand.CONNECT) {
            String jwtToken = accessor.getFirstNativeHeader("Authorization");
            if(jwtToken == null) {
                throw new AuctionApiException(SecurityErrorCode.VALIDATION_TOKEN);
            }
            Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
            sessionAttributes.put("user", jwtTokenProvider.getAuthentication(jwtToken.replace("Bearer ", "")));
            sessionAttributes.put("destination", new HashSet<String>());
        } else if(accessor.getCommand() == StompCommand.SUBSCRIBE) {
            Object user = accessor.getSessionAttributes().get("user");
            if(user == null) {
                return message;
            }
            String destination = accessor.getDestination();
            Set<String> destinationSet = (HashSet<String>) accessor.getSessionAttributes().get("destination");
            destinationSet.add(destination);

            SecurityContextHolder.getContext().setAuthentication((UsernamePasswordAuthenticationToken)user);

            Matcher matcher = PARTICIPANT.matcher(destination);
            if(matcher.matches()) {
                Long auctionSeq = Long.valueOf(matcher.group(1));
                auctionService.participate(auctionSeq);
            }
        } else if(accessor.getCommand() == StompCommand.DISCONNECT) {
            Set<String> destinationSet = (HashSet<String>) accessor.getSessionAttributes().get("destination");
            if(destinationSet == null || destinationSet.isEmpty()) {
                return message;
            }
            Object user = accessor.getSessionAttributes().get("user");
            if(user == null) {
                return message;
            }
            SecurityContextHolder.getContext().setAuthentication((UsernamePasswordAuthenticationToken)user);
            // 모든 연결을 끊어야 한다.
            for (String destination : destinationSet) {
                Matcher matcher = PARTICIPANT.matcher(destination);
                if(matcher.matches()) {
                    Long auctionSeq = Long.valueOf(matcher.group(1));
                    auctionService.exit(auctionSeq);
                }
            }
            destinationSet.clear();
        }

        return message;
    }
}
