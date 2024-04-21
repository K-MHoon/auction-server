package com.kmhoon.common.model.dto.service.inventory;

import com.kmhoon.common.enums.CouponStatus;
import com.kmhoon.common.enums.CouponType;
import com.kmhoon.common.model.entity.service.inventory.Coupon;
import com.kmhoon.common.utils.DateTimeUtil;
import lombok.*;
import org.hibernate.type.descriptor.DateTimeUtils;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Builder(access = AccessLevel.PRIVATE)
@Getter
public final class CouponDto {

    private Long sequence;
    private CouponType type;
    private CouponStatus status;
    private Boolean isUse;
    private LocalDateTime endDate;
    private String createdAt;
    private String updatedAt;

    public static CouponDto of(Coupon coupon) {
        return CouponDto.builder()
                .sequence(coupon.getSequence())
                .type(coupon.getType())
                .status(coupon.getStatus())
                .isUse(coupon.getIsUse())
                .endDate(coupon.getEndDate())
                .createdAt(DateTimeUtil.dateTimeToString(coupon.getCreatedAt()))
                .updatedAt(DateTimeUtil.dateTimeToString(coupon.getUpdatedAt()))
                .build();
    }
}
