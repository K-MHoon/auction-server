package com.kmhoon.common.model.entity.service.inventory;

import com.kmhoon.common.enums.CouponStatus;
import com.kmhoon.common.enums.CouponType;
import com.kmhoon.common.model.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Coupon coupon = (Coupon) o;
        return getSequence() != null && Objects.equals(getSequence(), coupon.getSequence());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
