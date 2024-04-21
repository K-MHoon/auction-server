package com.kmhoon.common.model.dto.service.item;

import com.kmhoon.common.enums.ItemType;
import com.kmhoon.common.model.entity.service.item.Item;
import lombok.*;

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

    public static ItemDto of(Item item) {
        return ItemDto.builder()
                .sequence(item.getSequence())
                .name(item.getName())
                .description(item.getDescription())
                .type(item.getType())
                .isUse(item.getIsUse())
                .build();
    }
}
