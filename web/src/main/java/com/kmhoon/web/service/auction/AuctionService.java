package com.kmhoon.web.service.auction;

import com.kmhoon.common.enums.AuctionStatus;
import com.kmhoon.common.model.entity.auth.user.User;
import com.kmhoon.common.model.entity.service.auction.Auction;
import com.kmhoon.common.model.entity.service.item.Item;
import com.kmhoon.common.repository.auth.user.UserRepository;
import com.kmhoon.common.repository.service.item.ItemRepository;
import com.kmhoon.common.repository.service.auction.AuctionRepository;
import com.kmhoon.web.exception.AuctionApiException;
import com.kmhoon.web.exception.code.service.item.ItemErrorCode;
import com.kmhoon.web.service.dto.PageResponseDto;
import com.kmhoon.web.service.dto.auction.request.AuctionServiceRequestDto;
import com.kmhoon.web.service.dto.auction.response.AuctionServiceResponseDto;
import com.kmhoon.web.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public void createAuction(AuctionServiceRequestDto.CreateAuctionServiceRequest request) {
        String loggedInUserId = SecurityUtils.getLoggedInUserId();
        User user = userRepository.findByEmailWithInventory(loggedInUserId).orElseThrow();

        Item item = itemRepository.findByInventoryAndSequenceAndIsUseIsTrue(user.getInventory(), request.getItemSeq())
                .orElseThrow(() -> new AuctionApiException(ItemErrorCode.HAS_NOT_SEQ_REQUEST));

        Auction auction = Auction.builder()
                .title(request.getTitle())
                .price(request.getPrice())
                .isUse(Boolean.TRUE)
                .description(request.getDescription())
                .minPrice(request.getMinPrice())
                .seller(user)
                .status(AuctionStatus.BEFORE)
                .item(item)
                .build();

        auctionRepository.save(auction);
    }
}
