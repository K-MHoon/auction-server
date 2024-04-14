package com.kmhoon.common.repository.service.inventory;

import com.kmhoon.common.model.entity.service.inventory.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
