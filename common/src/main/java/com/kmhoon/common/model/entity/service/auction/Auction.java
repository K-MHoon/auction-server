package com.kmhoon.common.model.entity.service.auction;

import com.kmhoon.common.enums.AuctionStatus;
import com.kmhoon.common.enums.AuctionType;
import com.kmhoon.common.model.entity.BaseEntity;
import com.kmhoon.common.model.entity.auth.user.User;
import com.kmhoon.common.model.entity.service.item.Item;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.type.NumericBooleanConverter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Enumerated(value = EnumType.STRING)
    private AuctionType auctionType;

    @Enumerated(value = EnumType.STRING)
    private AuctionStatus status;

    @Convert(converter = NumericBooleanConverter.class)
    private Boolean isUse;

    @Comment("경매시작시간")
    private LocalDateTime startTime;

    @Comment("경매종료시간")
    private LocalDateTime endTime;

    @Comment("판매완료시간")
    private LocalDateTime soldTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_seq")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_seq")
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_seq")
    private User buyer;

    @ElementCollection
    @Builder.Default
    private List<AuctionImage> auctionImageList = new ArrayList<>();

    public void addImage(String fileName) {
        AuctionImage auctionImage = AuctionImage.builder()
                .fileName(fileName)
                .ord(this.auctionImageList.size())
                .build();
        this.auctionImageList.add(auctionImage);
    }

    public void updateAuctionStatus(AuctionStatus auctionStatus) {
        this.status = auctionStatus;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Auction auction = (Auction) o;
        return getSequence() != null && Objects.equals(getSequence(), auction.getSequence());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
