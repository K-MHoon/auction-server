package com.kmhoon.common.model.entity.service.auction;

import com.kmhoon.common.enums.AuctionStatus;
import com.kmhoon.common.model.entity.BaseEntity;
import com.kmhoon.common.model.entity.BaseTimeEntity;
import com.kmhoon.common.model.entity.auth.user.User;
import com.kmhoon.common.model.entity.service.item.Item;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.type.NumericBooleanConverter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_service_auction")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Auction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long sequence;

    // 판매 제목
    private String title;

    // 최소 비용
    private Long minPrice;

    // 현재 금액 또는 판매 최종 금액
    private Long price;

    // 판매 글
    private String description;

    private AuctionStatus status;

    @Convert(converter = NumericBooleanConverter.class)
    private Boolean isUse;

    private LocalDateTime soldTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_seq", updatable = false, nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_seq", updatable = false, nullable = false)
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_seq", updatable = false, nullable = false)
    private User user;
}
