package com.kmhoon.web.service.dto.auction.response;

import com.kmhoon.common.model.dto.service.auction.AuctionDto;
import com.kmhoon.web.service.dto.PageResponseDto;
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
}
