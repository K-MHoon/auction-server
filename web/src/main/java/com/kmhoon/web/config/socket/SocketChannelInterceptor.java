package com.kmhoon.web.config.socket;

import com.kmhoon.web.exception.AuctionApiException;
import com.kmhoon.web.exception.code.security.SecurityErrorCode;
import com.kmhoon.web.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class SocketChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if(accessor.getCommand() == StompCommand.CONNECT) {
            String jwtToken = accessor.getFirstNativeHeader("Authorization");
            if(jwtToken == null) {
                throw new AuctionApiException(SecurityErrorCode.VALIDATION_TOKEN);
            }
            Claims claims = jwtTokenProvider.validate(jwtToken);
            log.info("Connect = {}", claims.get("email"));
        }

        return message;
    }
}
