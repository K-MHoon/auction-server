package com.kmhoon.web.service.dto.inventory.request;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CouponServiceRequestDto {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    @Getter
    @ToString
    public static class Buy {

        private Long count;
        private Long price;
    }
}
