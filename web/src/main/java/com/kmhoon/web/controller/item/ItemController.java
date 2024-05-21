package com.kmhoon.web.controller.item;

import com.kmhoon.common.model.dto.service.auction.AuctionDto;
import com.kmhoon.common.model.dto.service.item.ItemDto;
import com.kmhoon.web.controller.dto.item.request.ItemControllerRequest;
import com.kmhoon.web.service.dto.PageResponseDto;
import com.kmhoon.web.service.dto.item.ItemServiceResponseDto;
import com.kmhoon.web.service.item.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    @GetMapping("/api/view/item/{fileName}")
    public ResponseEntity<Resource> viewFileGet(@PathVariable(name = "fileName") String fileName) {
        return itemService.view(fileName);
    }

    @PostMapping("/api/service/item")
    @ResponseStatus(HttpStatus.OK)
    public void add(@Valid ItemControllerRequest.Add request) {
        log.info("add : " + request);
        itemService.add(request.toServiceRequest());
    }

    @GetMapping("/api/service/item/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto get(@PathVariable(value = "itemId") Long itemId) {
        log.info("get : " + itemId);
        return itemService.get(itemId);
    }

    @DeleteMapping("/api/service/item")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestBody @Valid ItemControllerRequest.Delete request) {
        log.info("delete : " + request);
        itemService.delete(request);
    }

    @GetMapping("/api/service/item/{itemId}/history")
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto<AuctionDto> getHistory(@PathVariable(value = "itemId") Long itemId, Pageable pageable) {
        log.info("get History : " + itemId);
        return itemService.getHistory(itemId, pageable);
    }

    @GetMapping("/api/service/item/{itemId}/history/auction/{auctionId}")
    @ResponseStatus(HttpStatus.OK)
    public AuctionDto getAuctionHistory(@PathVariable(value = "itemId") Long itemId, @PathVariable(value = "auctionId") Long auctionId) {
        return itemService.getHistoryDetail(itemId, auctionId);
    }
}
