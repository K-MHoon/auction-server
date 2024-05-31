package com.kmhoon.web.service.dto.auction.request;

import com.kmhoon.common.enums.AuctionType;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private Long itemSeq;
        @Builder.Default
        private List<MultipartFile> images = new ArrayList<>();
    }
}
