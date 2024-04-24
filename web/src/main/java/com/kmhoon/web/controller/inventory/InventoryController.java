package com.kmhoon.web.controller.inventory;


import com.kmhoon.web.service.dto.inventory.response.InventoryServiceResponseDto;
import com.kmhoon.web.service.inventory.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryService service;

    @GetMapping("/api/service/inventory")
    @ResponseStatus(HttpStatus.OK)
    public InventoryServiceResponseDto.GetInventory getInventory(Principal principal) {
        return service.getInventory();
    }
}
