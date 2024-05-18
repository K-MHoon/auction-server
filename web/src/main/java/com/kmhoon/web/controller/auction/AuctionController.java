package com.kmhoon.web.controller.auction;

import com.kmhoon.common.model.dto.service.auction.AuctionDto;
import com.kmhoon.web.controller.dto.auction.CreateAuctionRequest;
import com.kmhoon.web.service.auction.AuctionService;
import com.kmhoon.web.service.dto.auction.response.AuctionServiceResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuctionController {

    private final AuctionService auctionService;
    @PostMapping("/api/service/auctions")
    @ResponseStatus(HttpStatus.OK)
    public void createAuction(Principal principal, @RequestBody @Valid CreateAuctionRequest request) {

        auctionService.createAuction(request.toServiceRequest());
    }
}
