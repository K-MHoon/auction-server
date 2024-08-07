package com.kmhoon.web.socket.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AuctionParticipantDto.class, name = "PARTICIPANT"),
        @JsonSubTypes.Type(value = AuctionPriceDto.class, name = "PRICE"),
        @JsonSubTypes.Type(value = AuctionStatusDto.class, name = "STATUS"),
        @JsonSubTypes.Type(value = AuctionPriceUserHistoryDto.class, name = "PRICE-HISTORY"),
})
public interface RedisMessage {

    String getType();
    Object getData();
}
