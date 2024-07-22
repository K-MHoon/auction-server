package com.kmhoon.common.repository.service.item;

import com.kmhoon.common.model.entity.service.inventory.Inventory;
import com.kmhoon.common.model.entity.service.item.Item;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByInventoryAndIsUseIsTrue(Inventory inventory);

    @EntityGraph(attributePaths = {"auctionList"})
    Optional<Item> findByInventoryAndSequenceAndIsUseIsTrue(Inventory inventory, Long seq);

    int countByInventoryAndIsUseIsTrue(Inventory inventory);
}
