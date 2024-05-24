package com.kmhoon.web.controller.dto.auction;

import com.kmhoon.common.enums.AuctionType;
import com.kmhoon.web.service.dto.auction.request.AuctionServiceRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@ToString
public class CreateAuctionRequest {

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
    private String startTime;

    @NotBlank
    private String endTime;

    @NotNull
    private MultipartFile image;

    public AuctionServiceRequestDto.CreateAuctionServiceRequest toServiceRequest() {
        return AuctionServiceRequestDto.CreateAuctionServiceRequest.builder()
                .title(this.title)
                .minPrice(this.minPrice)
                .description(this.description)
                .itemSeq(this.itemSeq)
                .type(this.type)
                .image(this.image)
                .build();
    }
}
