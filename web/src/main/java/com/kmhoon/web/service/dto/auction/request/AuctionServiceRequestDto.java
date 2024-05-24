package com.kmhoon.web.service.dto.auction.request;

import com.kmhoon.common.enums.AuctionType;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
        private AuctionType type;
        private String description;
        private String startTime;
        private String endTime;
        private Long itemSeq;
        private MultipartFile image;
    }
}
