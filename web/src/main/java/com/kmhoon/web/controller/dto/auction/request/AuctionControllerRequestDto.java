package com.kmhoon.web.controller.dto.auction.request;

import com.kmhoon.common.enums.AuctionType;
import com.kmhoon.web.service.dto.auction.request.AuctionServiceRequestDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuctionControllerRequestDto {


    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    @Builder
    @ToString
    public static final class CreateAuction {

        @NotBlank
        private String title;

        @NotNull
        private Long minPrice;

        @NotBlank
        private String description;

        @NotNull
        private AuctionType type;

        @NotNull
        private Long itemSeq;

        @NotBlank
        private LocalDateTime startTime;

        @NotBlank
        private LocalDateTime endTime;

        @NotNull
        @Builder.Default
        private List<MultipartFile> images = new ArrayList<>();

        public AuctionServiceRequestDto.CreateAuction toServiceRequest() {
            return AuctionServiceRequestDto.CreateAuction.builder()
                    .title(this.title)
                    .minPrice(this.minPrice)
                    .description(this.description)
                    .itemSeq(this.itemSeq)
                    .startTime(this.startTime)
                    .endTime(this.endTime)
                    .type(this.type)
                    .images(this.images)
                    .build();
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    @Builder
    @ToString
    public static final class UpdatePrice {

        @Min(0)
        @NotNull
        private Long price;
    }
}
