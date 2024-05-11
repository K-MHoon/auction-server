package com.kmhoon.web.service.dto.auction.request;

import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuctionServiceRequestDto {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    @ToString
    @Builder
    public static class CreateAuctionServiceRequest {

        private String title;
        private Long minPrice;
        private Long price;
        private String description;
        private Long itemSeq;
    }
}
