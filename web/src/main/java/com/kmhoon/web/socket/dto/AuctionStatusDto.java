package com.kmhoon.web.socket.dto;

import com.kmhoon.common.enums.AuctionStatus;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuctionStatusDto implements RedisMessage {

    private String type;
    private AuctionStatusMessage data;

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static final class AuctionStatusMessage {

        private Long auctionSeq;
        private AuctionStatus auctionStatus;
    }

    public static AuctionStatusDto create(AuctionStatusMessage message) {
        return new AuctionStatusDto("STATUS", message);
    }

}
