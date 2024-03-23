package com.kmhoon.common.repository.service.inventory;

import com.kmhoon.common.model.entity.service.inventory.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
