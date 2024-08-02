package com.kmhoon.common.model.entity.service.item;

import com.kmhoon.common.enums.ItemType;
import com.kmhoon.common.model.entity.BaseEntity;
import com.kmhoon.common.model.entity.service.auction.Auction;
import com.kmhoon.common.model.entity.service.inventory.Inventory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.type.NumericBooleanConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @ElementCollection
    @Builder.Default
    private List<ItemImage> imageList = new ArrayList<>();

    @ElementCollection
    @Builder.Default
    private List<ItemDocument> documentList = new ArrayList<>();

    /**
     * 해당 아이템이 진행한 경매 이력을 확인 할 수 있다.
     */
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Auction> auctionList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_seq")
    private Inventory inventory;

    public void addImage(String fileName) {
        ItemImage itemImage = ItemImage.builder()
                .fileName(fileName)
                .ord(this.imageList.size())
                .build();
        this.imageList.add(itemImage);
    }

    public void addDocument(String fileName) {
        ItemDocument document = ItemDocument.builder()
                .fileName(fileName)
                .ord(this.documentList.size())
                .build();
        this.documentList.add(document);
    }

    public void delete() {
        this.isUse = Boolean.FALSE;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Item item = (Item) o;
        return getSequence() != null && Objects.equals(getSequence(), item.getSequence());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    public void updateInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
