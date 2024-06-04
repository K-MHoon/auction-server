package com.kmhoon.common.repository.service.inventory;

import com.kmhoon.common.enums.CouponStatus;
import com.kmhoon.common.model.entity.service.inventory.Coupon;
import com.kmhoon.common.model.entity.service.inventory.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    List<Coupon> findAllByInventoryAndIsUseIsTrueAndStatus(Inventory inventory, CouponStatus status);

}
