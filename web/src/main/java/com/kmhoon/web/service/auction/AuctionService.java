package com.kmhoon.web.service.auction;

import com.kmhoon.common.enums.AuctionStatus;
import com.kmhoon.common.enums.CouponStatus;
import com.kmhoon.common.model.entity.auth.user.User;
import com.kmhoon.common.model.entity.service.auction.Auction;
import com.kmhoon.common.model.entity.service.inventory.Coupon;
import com.kmhoon.common.model.entity.service.item.Item;
import com.kmhoon.common.repository.service.auction.AuctionRepository;
import com.kmhoon.common.repository.service.inventory.CouponRepository;
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

import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.kmhoon.web.exception.code.auction.AuctionErrorCode.BETWEEN_ONE_WEEK;
import static com.kmhoon.web.exception.code.auction.AuctionErrorCode.START_DATE_NOT_GE_END_DATE;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final ItemRepository itemRepository;
    private final UserCommonService userCommonService;
    private final CouponRepository couponRepository;
    private final CustomFileUtil fileUtil;

    @Transactional
    public void createAuction(AuctionServiceRequestDto.CreateAuctionServiceRequest request) {
        User loggedInUser = userCommonService.getLoggedInUser();
        Item item = itemRepository.findByInventoryAndSequenceAndIsUseIsTrue(loggedInUser.getInventory(), request.getItemSeq())
                .orElseThrow(() -> new AuctionApiException(ItemErrorCode.HAS_NOT_SEQ_REQUEST));

        List<Coupon> couponList = couponRepository.findAllByInventoryAndIsUseIsTrueAndStatus(loggedInUser.getInventory(), CouponStatus.UNUSED);

        if(couponList.isEmpty()) {
            throw new AuctionApiException(AuctionErrorCode.HAS_NOT_COUPON);
        }

        // 이미 진행중인 경매가 있는 경우 신규 경매 생성 불가
        if(item.getAuctionList().stream().anyMatch(auction -> auction.getStatus() != AuctionStatus.FINISHED)) {
            throw new AuctionApiException(AuctionErrorCode.NOT_FINISHED_AUCTION_EXIST);
        }

        if(request.getStartTime().isEqual(request.getEndTime())
                || request.getStartTime().isAfter(request.getEndTime())) {
            throw new AuctionApiException(START_DATE_NOT_GE_END_DATE);
        }

        if(ChronoUnit.WEEKS.between(request.getStartTime(), request.getEndTime()) > 1) {
            throw new AuctionApiException(BETWEEN_ONE_WEEK);
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
