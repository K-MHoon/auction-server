package com.kmhoon.common.repository.service.auction;

import com.kmhoon.common.enums.AuctionStatus;
import com.kmhoon.common.model.entity.service.auction.Auction;
import com.kmhoon.common.model.entity.service.inventory.Inventory;
import com.kmhoon.common.model.entity.service.item.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AuctionRepository extends JpaRepository<Auction, Long>, AuctionRepositorySupport {
    @EntityGraph(attributePaths = {"item", "seller", "buyer"})
    Page<Auction> findAllByItemAndStatusAndIsUseOrderByCreatedAtDesc(Item item, AuctionStatus status, Boolean isUse, Pageable pageable);
    Optional<Auction> findBySequenceAndItem_SequenceAndItem_InventoryAndIsUseIsTrueAndStatus(Long seq, Long itemSeq, Inventory inventory, AuctionStatus status);

    @EntityGraph(attributePaths = {"auctionImageList", "seller"})
    List<Auction> findTop10ByIsUseIsTrueAndStatusOrderByStartTimeDesc(AuctionStatus auctionStatus);

    List<Auction> findAllByStartTimeAndIsUseIsTrueAndStatus(LocalDateTime startTime, AuctionStatus auctionStatus);
    List<Auction> findAllByEndTimeAndIsUseIsTrueAndStatus(LocalDateTime startTime, AuctionStatus auctionStatus);
}
