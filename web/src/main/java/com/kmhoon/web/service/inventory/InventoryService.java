package com.kmhoon.web.service.inventory;

import com.kmhoon.common.model.entity.auth.user.User;
import com.kmhoon.common.model.entity.service.inventory.Coupon;
import com.kmhoon.common.model.entity.service.inventory.Inventory;
import com.kmhoon.common.model.entity.service.item.Item;
import com.kmhoon.common.repository.service.inventory.CouponRepository;
import com.kmhoon.common.repository.service.inventory.InventoryRepository;
import com.kmhoon.common.repository.service.item.ItemRepository;
import com.kmhoon.web.service.user.UserCommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.kmhoon.web.service.dto.inventory.response.InventoryServiceResponseDto.GetInventory;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    private final ItemRepository itemRepository;
    private final CouponRepository couponRepository;
    private final UserCommonService userCommonService;

    @Transactional(readOnly = true)
    public GetInventory getInventory() {
        User loggedInUser = userCommonService.getLoggedInUser();
        Inventory inventory = loggedInUser.getInventory();
        List<Item> itemList = itemRepository.findAllByInventoryAndIsUseIsTrue(inventory);
        List<Coupon> couponList = couponRepository.findAllByInventoryAndIsUseIsTrue(inventory);

        return GetInventory.of(inventory.getMoney(), couponList, itemList);
    }
}
