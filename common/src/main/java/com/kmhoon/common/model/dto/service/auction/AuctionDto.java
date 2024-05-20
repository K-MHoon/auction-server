package com.kmhoon.common.model.dto.service.auction;

import com.kmhoon.common.enums.AuctionStatus;
import com.kmhoon.common.model.dto.auth.user.UserDto;
import com.kmhoon.common.model.dto.service.item.ItemDto;
import com.kmhoon.common.model.entity.service.auction.Auction;
import com.kmhoon.common.utils.DateTimeUtil;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
@Getter
public final class AuctionDto {

    private Long sequence;
    private String title;
    private Long minPrice;
    private Long price;
    private String description;
    private AuctionStatus status;
    private Boolean isUse;
    private String soldTime;
    private ItemDto item;
    private UserDto.Simple seller;
    private UserDto.Simple buyer;

    public static AuctionDto of(Auction auction) {
        return AuctionDto.builder()
                .sequence(auction.getSequence())
                .title(auction.getTitle())
                .minPrice(auction.getMinPrice())
                .price(auction.getPrice())
                .description(auction.getDescription())
                .status(auction.getStatus())
                .isUse(auction.getIsUse())
                .soldTime(DateTimeUtil.dateTimeToString(auction.getSoldTime()))
                .item(ItemDto.ofSimple(auction.getItem()))
                .seller(UserDto.Simple.of(auction.getSeller()))
                .buyer(UserDto.Simple.of(auction.getBuyer()))
                .build();
    }

    public static AuctionDto forHistory(Auction auction) {
        return AuctionDto.builder()
                .sequence(auction.getSequence())
                .title(auction.getTitle())
                .minPrice(auction.getMinPrice())
                .price(auction.getPrice())
                .status(auction.getStatus())
                .soldTime(DateTimeUtil.dateTimeToString(auction.getSoldTime()))
                .seller(UserDto.Simple.of(auction.getSeller()))
                .buyer(UserDto.Simple.of(auction.getBuyer()))
                .build();
    }

}