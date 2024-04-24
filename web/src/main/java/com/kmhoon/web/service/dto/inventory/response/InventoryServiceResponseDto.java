package com.kmhoon.web.service.dto.inventory.response;

import com.kmhoon.common.model.dto.service.inventory.CouponDto;
import com.kmhoon.common.model.dto.service.item.ItemDto;
import com.kmhoon.common.model.entity.service.inventory.Coupon;
import com.kmhoon.common.model.entity.service.item.Item;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class InventoryServiceResponseDto {


    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public final static class GetInventory {

        private Long money;
        private List<CouponDto> couponList;
        private List<ItemDto> itemList;

        public static GetInventory of(Long money, List<Coupon> couponList, List<Item> itemList) {
            return GetInventory.builder()
                    .money(money)
                    .couponList(couponList.stream()
                            .map(CouponDto::of)
                            .collect(Collectors.toList()))
                    .itemList(itemList.stream()
                            .map(ItemDto::of)
                            .collect(Collectors.toList()))
                    .build();
        }
    }
}
