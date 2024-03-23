package com.kmhoon.common.model.entity.service.inventory;

import com.kmhoon.common.model.entity.BaseEntity;
import com.kmhoon.common.model.entity.BaseTimeEntity;
import com.kmhoon.common.model.entity.auth.user.User;
import com.kmhoon.common.model.entity.service.item.Item;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_service_inventory")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Inventory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sequence;

    @OneToMany(mappedBy = "inventory")
    @Builder.Default
    private List<Item> itemList = new ArrayList<>();

    @OneToOne(mappedBy = "inventory")
    private User owner;

    // 경매 가능한 쿠폰 수
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "inventory")
    @Builder.Default
    private List<Coupon> couponList = new ArrayList<>();

    private Long money;
}
