package com.kmhoon.common.model.entity.service.inventory;

import com.kmhoon.common.enums.CouponStatus;
import com.kmhoon.common.enums.CouponType;
import com.kmhoon.common.model.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_service_inventory_coupon")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Coupon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sequence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_seq")
    private Inventory inventory;

    @Enumerated(EnumType.STRING)
    @Comment(value="쿠폰 종류")
    private CouponType type;

    // 쿠폰 현재 상태
    @Enumerated(EnumType.STRING)
    @Comment(value="쿠폰 현재 상태")
    private CouponStatus status;

    @Comment(value = "사용 완료된 날짜")
    private LocalDateTime endDate;

    @Comment(value = "삭제 여부")
    private Boolean isUse;
}
