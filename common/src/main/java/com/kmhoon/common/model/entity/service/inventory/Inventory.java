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
public class Inventory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sequence;

    @OneToMany(mappedBy = "inventory")
    @Builder.Default
    private List<Item> itemList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_seq")
    private User owner;

    // 경매 가능한 쿠폰 수
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "inventory")
    @Builder.Default
    private List<Coupon> couponList = new ArrayList<>();

    private Long money;

    public void updateMoney(long money) {
        if(this.money > 0) {
            throw new IllegalArgumentException("잔액은 0보다 작을 수 없습니다.");
        }
        this.money = money;
    }

    public void minusMoney(long money) {
        if(this.money < money) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }
        this.money -= money;
    }
}
