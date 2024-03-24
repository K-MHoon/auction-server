package com.kmhoon.web.controller.dto.inventory.request;

import com.kmhoon.web.service.dto.inventory.request.CouponServiceRequestDto;
import jakarta.validation.constraints.NotNull;
import lombok.*;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CouponControllerRequest {

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Buy {

        @NotNull
        private Long count;

        public CouponServiceRequestDto.Buy toServiceRequest() {
            return CouponServiceRequestDto.Buy.builder()
                    .count(this.count)
                    .build();
        }
    }

}
