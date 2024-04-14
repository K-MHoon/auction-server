package com.kmhoon.common.repository.item;

import com.kmhoon.common.model.entity.service.inventory.Inventory;
import com.kmhoon.common.model.entity.service.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllBySequenceInAndInventoryAndIsUse(List<Long> seqList, Inventory inventory, Boolean isUse);
}
