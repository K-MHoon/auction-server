package com.kmhoon.web.service.dto.auction.response;

import com.kmhoon.common.enums.AuctionStatus;
import com.kmhoon.common.model.entity.service.auction.Auction;
import com.kmhoon.web.service.dto.PageResponseDto;
import com.kmhoon.common.utils.DateTimeUtil;
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

        PageResponseDto page;
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
        private String sellerEmail;
        private String sellerName;
        private String createdAt;

        public static AuctionDto buildFrom(Auction auction) {
            return AuctionDto.builder()
                    .sequence(auction.getSequence())
                    .title(auction.getTitle())
                    .minPrice(auction.getMinPrice())
                    .price(auction.getPrice())
                    .description(auction.getDescription())
                    .status(auction.getStatus())
                    .isUse(auction.getIsUse())
                    .sellerEmail(auction.getSeller().getEmail())
                    .sellerName(auction.getSeller().getName())
                    .createdAt(DateTimeUtil.dateTimeToString(auction.getCreatedAt()))
                    .build();
        }
    }
}
