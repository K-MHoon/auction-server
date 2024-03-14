package com.kmhoon.common.model.entity.service.item;

import com.kmhoon.common.enums.ItemType;
import com.kmhoon.common.model.entity.BaseEntity;
import com.kmhoon.common.model.entity.service.auction.Auction;
import com.kmhoon.common.model.entity.service.inventory.Inventory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.type.NumericBooleanConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * 경매장에 올리는 물건
 */
@Entity
@Table(name = "tb_service_item")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long sequence;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, updatable = false)
    private ItemType type;

    @Convert(converter = NumericBooleanConverter.class)
    private Boolean isUse;

    // 이미지

    /**
     * 경매 이력을 확인 할 수 있다.
     */
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Auction> auctionList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_seq")
    private Inventory inventory;
}
