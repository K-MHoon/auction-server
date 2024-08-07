package com.kmhoon.web.socket.dto;

import lombok.*;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuctionPriceUserHistoryDto implements RedisMessage {

    private String type;
    private AuctionPriceUserHistoryMessage data;

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static final class AuctionPriceUserHistoryMessage {

        private Long auctionSeq;
        private Long userSeq;
        private List<PriceHistory> priceHistoryList;
    }

    public static AuctionPriceUserHistoryDto create(AuctionPriceUserHistoryMessage message) {
        return new AuctionPriceUserHistoryDto("PRICE-HISTORY", message);
    }

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static final class PriceHistory {

        private Long price;
        private String date;
    }
}
