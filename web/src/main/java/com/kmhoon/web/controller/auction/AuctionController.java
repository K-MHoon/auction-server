package com.kmhoon.web.controller.auction;

import com.kmhoon.common.enums.ItemType;
import com.kmhoon.common.model.dto.service.auction.AuctionDto;
import com.kmhoon.web.controller.dto.auction.CreateAuctionRequest;
import com.kmhoon.web.service.auction.AuctionService;
import com.kmhoon.web.service.dto.PageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuctionController {

    private final AuctionService auctionService;

    @PostMapping("/api/service/auction")
    @ResponseStatus(HttpStatus.OK)
    public void createAuction(@ModelAttribute CreateAuctionRequest request) {
        auctionService.createAuction(request.toServiceRequest());
    }

    @GetMapping("/api/service/auction/slider")
    @ResponseStatus(HttpStatus.OK)
    public List<AuctionDto> getAuctionSliderList() {
        return auctionService.getAuctionSliderList();
    }

    @GetMapping("/api/service/auction")
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto<AuctionDto> getAuctionList(@RequestParam(value = "item-type", required = false) ItemType itemType,
                                                      @RequestParam(value = "item-name", required = false) String itemName,
                                                      @PageableDefault Pageable pageable) {
        return auctionService.getAuctionList(itemType, itemName, pageable);
    }
}
