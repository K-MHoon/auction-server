package com.kmhoon.common.model.dto.service.item;

import com.kmhoon.common.enums.ItemType;
import com.kmhoon.common.model.dto.service.auction.AuctionDto;
import com.kmhoon.common.model.entity.service.item.Item;
import com.kmhoon.common.model.entity.service.item.ItemDocument;
import com.kmhoon.common.model.entity.service.item.ItemImage;
import com.kmhoon.common.utils.DateTimeUtil;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@ToString
@Getter
public final class ItemDto {

    private Long sequence;
    private String name;
    private String description;
    private ItemType type;
    private Boolean isUse;
    private String createdBy;
    private String createdAt;
    private List<ItemImage> itemImageList;
    private List<ItemDocument> itemDocumentList;
    private List<AuctionDto> auctionHistory;

    public static ItemDto ofSimple(Item item) {
        return ItemDto.builder()
                .sequence(item.getSequence())
                .name(item.getName())
                .description(item.getDescription())
                .type(item.getType())
                .isUse(item.getIsUse())
                .itemImageList(item.getImageList())
                .build();
    }

    public static ItemDto ofDetail(Item item) {
        return ItemDto.builder()
                .sequence(item.getSequence())
                .name(item.getName())
                .description(item.getDescription())
                .type(item.getType())
                .isUse(item.getIsUse())
                .createdBy(item.getCreatedBy())
                .createdAt(DateTimeUtil.dateTimeToString(item.getCreatedAt()))
                .itemImageList(item.getImageList())
                .itemDocumentList(item.getDocumentList())
                .auctionHistory(item.getAuctionList().stream().map(AuctionDto::forHistory).collect(Collectors.toList()))
                .build();
    }
}
