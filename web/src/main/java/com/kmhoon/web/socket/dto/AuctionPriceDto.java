package com.kmhoon.web.socket.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuctionPriceDto implements RedisMessage {

    private String type;
    private AuctionPriceMessage data;

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static final class AuctionPriceMessage {

        private Long auctionSeq;
        private Long userSeq;
        private Long price;
    }

    public static AuctionPriceDto create(AuctionPriceMessage message) {
        return new AuctionPriceDto("PRICE", message);
    }

}
