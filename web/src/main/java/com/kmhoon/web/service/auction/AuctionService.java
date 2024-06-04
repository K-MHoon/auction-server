package com.kmhoon.web.service.auction;

import com.kmhoon.common.enums.AuctionStatus;
import com.kmhoon.common.model.entity.auth.user.User;
import com.kmhoon.common.model.entity.service.auction.Auction;
import com.kmhoon.common.model.entity.service.item.Item;
import com.kmhoon.common.repository.service.auction.AuctionRepository;
import com.kmhoon.common.repository.service.item.ItemRepository;
import com.kmhoon.web.exception.AuctionApiException;
import com.kmhoon.web.exception.code.auction.AuctionErrorCode;
import com.kmhoon.web.exception.code.service.item.ItemErrorCode;
import com.kmhoon.web.service.dto.auction.request.AuctionServiceRequestDto;
import com.kmhoon.web.service.user.UserCommonService;
import com.kmhoon.web.utils.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final ItemRepository itemRepository;
    private final UserCommonService userCommonService;
    private final CustomFileUtil fileUtil;

    @Transactional
    public void createAuction(AuctionServiceRequestDto.CreateAuctionServiceRequest request) {
        User loggedInUser = userCommonService.getLoggedInUser();
        Item item = itemRepository.findByInventoryAndSequenceAndIsUseIsTrue(loggedInUser.getInventory(), request.getItemSeq())
                .orElseThrow(() -> new AuctionApiException(ItemErrorCode.HAS_NOT_SEQ_REQUEST));

        // 이미 진행중인 경매가 있는 경우 신규 경매 생성 불가
        if(item.getAuctionList().stream().anyMatch(auction -> auction.getStatus() != AuctionStatus.FINISHED)) {
            throw new AuctionApiException(AuctionErrorCode.NOT_FINISHED_AUCTION_EXIST);
        }

        List<String> uploadImageFileNames = fileUtil.saveFiles(request.getImages());

        Auction auction = Auction.builder()
                .title(request.getTitle())
                .isUse(Boolean.TRUE)
                .description(request.getDescription())
                .minPrice(request.getMinPrice())
                .seller(loggedInUser)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(AuctionStatus.BEFORE)
                .item(item)
                .build();

        uploadImageFileNames.forEach(auction::addImage);

        auctionRepository.save(auction);
    }

}
