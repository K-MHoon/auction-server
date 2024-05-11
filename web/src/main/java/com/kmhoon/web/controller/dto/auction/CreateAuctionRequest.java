package com.kmhoon.web.controller.dto.auction;

import com.kmhoon.web.service.dto.auction.request.AuctionServiceRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@ToString
public class CreateAuctionRequest {

    @NotBlank
    private String title;

    @NotNull
    private Long minPrice;

    @NotNull
    private Long price;

    @NotBlank
    private String description;

    @NotNull
    private Long itemSeq;

    public AuctionServiceRequestDto.CreateAuctionServiceRequest toServiceRequest() {
        return AuctionServiceRequestDto.CreateAuctionServiceRequest.builder()
                .title(this.title)
                .minPrice(this.minPrice)
                .price(this.price)
                .description(this.description)
                .itemSeq(this.itemSeq)
                .build();
    }
}
