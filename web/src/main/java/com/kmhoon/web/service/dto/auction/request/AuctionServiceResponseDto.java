package com.kmhoon.web.service.dto.auction.request;

import com.kmhoon.common.enums.AuctionStatus;
import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuctionServiceResponseDto {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    @ToString
    @Builder
    public static final class GetAuctionList {

        List<AuctionDto> auctionList;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    @ToString
    @Builder
    public static class AuctionDto {

        private Long sequence;
        private String title;
        private Long minPrice;
        private Long price;
        private String description;
        private AuctionStatus status;
        private Boolean isUse;
        private String sellerId;
    }
}
