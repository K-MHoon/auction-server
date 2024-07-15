package com.kmhoon.web.controller.auction;

import com.kmhoon.common.enums.AuctionStatus;
import com.kmhoon.common.enums.AuctionType;
import com.kmhoon.common.enums.ItemType;
import com.kmhoon.common.model.dto.service.auction.AuctionDto;
import com.kmhoon.web.controller.dto.auction.request.AuctionControllerRequestDto;
import com.kmhoon.web.service.auction.AuctionService;
import com.kmhoon.web.service.dto.PageResponseDto;
import com.kmhoon.web.service.dto.auction.request.AuctionServiceRequestDto;
import jakarta.validation.Valid;
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
    public void createAuction(@ModelAttribute AuctionControllerRequestDto.CreateAuction request) {
        auctionService.createAuction(request.toServiceRequest());
    }

    @GetMapping("/api/service/auction/slider")
    @ResponseStatus(HttpStatus.OK)
    public List<AuctionDto> getAuctionSliderList() {
        return auctionService.getAuctionSliderList();
    }

    @GetMapping("/api/service/auction/my")
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto<AuctionDto> getMyAuctionList(@RequestParam(value = "auction-status", required = false) AuctionStatus auctionStatus,
                                                        @RequestParam(value = "auction-type", required = false) AuctionType auctionType,
                                                        @RequestParam(value = "item-type", required = false) ItemType itemType,
                                                        @RequestParam(value = "item-name", required = false) String itemName,
                                                        @PageableDefault Pageable pageable) {

        AuctionServiceRequestDto.GetMyAuctionList request = AuctionServiceRequestDto.GetMyAuctionList.builder()
                .auctionStatus(auctionStatus)
                .auctionType(auctionType)
                .itemType(itemType)
                .itemName(itemName)
                .pageable(pageable)
                .build();

        return auctionService.getMyAuctionList(request);
    }

    @GetMapping("/api/service/auction")
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto<AuctionDto> getAuctionList(@RequestParam(value = "item-type", required = false) ItemType itemType,
                                                      @RequestParam(value = "item-name", required = false) String itemName,
                                                      @PageableDefault Pageable pageable) {
        return auctionService.getAuctionList(itemType, itemName, pageable);
    }

    @PostMapping("/api/service/auction/{seq}/participate")
    @ResponseStatus(HttpStatus.OK)
    public void participate(@PathVariable("seq") Long auctionSeq) {
        auctionService.participate(auctionSeq);
    }

    @GetMapping("/api/service/auction/{seq}")
    @ResponseStatus(HttpStatus.OK)
    public AuctionDto getAuction(@PathVariable("seq") Long auctionSeq) {
        return auctionService.getAuction(auctionSeq);
    }

    @PostMapping("/api/service/auction/{seq}/price")
    @ResponseStatus(HttpStatus.OK)
    public void updatePrice(@PathVariable("seq") Long auctionSeq,
                            @RequestBody @Valid AuctionControllerRequestDto.UpdatePrice request) {
        auctionService.updatePrice(auctionSeq, request.getPrice());
    }
}
