package com.kmhoon.web.config.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmhoon.web.exception.AuctionApiException;
import com.kmhoon.web.socket.dto.RedisMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisMessageListener implements MessageListener {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper mapper;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishMessage = this.redisTemplate.getStringSerializer().deserialize(message.getBody());
            RedisMessage redisMessage = this.mapper.readValue(publishMessage, RedisMessage.class);
            this.simpMessagingTemplate.convertAndSend(new String(message.getChannel()), redisMessage.getData());
        } catch (Exception e) {
            throw new AuctionApiException("Socket Message Exception", e);
        }
    }
}
