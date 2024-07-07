package com.kmhoon.web.service.auction;

import com.kmhoon.common.enums.AuctionStatus;
import com.kmhoon.common.enums.CouponStatus;
import com.kmhoon.common.enums.ItemType;
import com.kmhoon.common.model.dto.service.auction.AuctionDto;
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
import com.kmhoon.web.service.dto.PageResponseDto;
import com.kmhoon.web.service.dto.auction.request.AuctionServiceRequestDto;
import com.kmhoon.web.service.user.UserCommonService;
import com.kmhoon.web.socket.dto.AuctionParticipantDto;
import com.kmhoon.web.utils.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.ObjLongConsumer;

import static com.kmhoon.web.exception.code.auction.AuctionErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final ItemRepository itemRepository;
    private final UserCommonService userCommonService;
    private final CouponRepository couponRepository;
    private final CustomFileUtil fileUtil;
    private final RedisTemplate<String, Object> redisTemplate;

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
                .priceUnit(request.getPriceUnit())
                .maxParticipantCount(request.getMaxParticipantCount())
                .build();

        uploadImageFileNames.forEach(auction::addImage);

        auctionRepository.save(auction);
    }

    @Transactional(readOnly = true)
    public List<AuctionDto> getAuctionSliderList() {
        List<Auction> runningAuctionList = auctionRepository.findTop10ByIsUseIsTrueAndStatusOrderByStartTimeDesc(AuctionStatus.RUNNING);
        return runningAuctionList.stream().map(AuctionDto::forSimple).toList();
    }

    @Transactional(readOnly = true)
    public PageResponseDto<AuctionDto> getAuctionList(ItemType itemType, String itemName, Pageable pageable) {
        Page<Auction> auctionList = auctionRepository.findAllByItemTypeAndItemNameAndPageable(itemType, itemName, pageable);
        List<AuctionDto> dtoList = auctionList.stream().map(AuctionDto::forList).toList();
        return toPageResponseDto(dtoList, pageable, auctionList);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<AuctionDto> getMyAuctionList(AuctionServiceRequestDto.GetMyAuctionListServiceRequest request) {
        Page<Auction> auctionList = auctionRepository.findAllByMyAuctionList(request.getAuctionStatus(), request.getAuctionType(), request.getItemType(), request.getItemName(), userCommonService.getLoggedInUser(), request.getPageable());
        List<AuctionDto> dtoList = auctionList.stream().map(AuctionDto::forMyList).toList();
        return toPageResponseDto(dtoList, request.getPageable(), auctionList);
    }

    private static PageResponseDto<AuctionDto> toPageResponseDto(List<AuctionDto> dtoList, Pageable request, Page<Auction> auctionList) {
        return PageResponseDto.<AuctionDto>withAll()
                .dtoList(dtoList)
                .pageable(request)
                .totalCount(auctionList.getTotalElements())
                .build();
    }

    @Transactional(readOnly = true)
    public AuctionDto getAuction(Long auctionSeq) {
        Auction auction = auctionRepository.findBySequenceAndIsUseIsTrueAndStatus(auctionSeq, AuctionStatus.RUNNING)
                .orElseThrow(() -> new AuctionApiException(AUCTION_NOT_FOUND));
        return AuctionDto.forMyList(auction);
    }

    @Transactional
    public void exit(Long auctionSeq) {
        joinTemplate(auctionSeq, (key, seq) -> redisTemplate.opsForZSet().remove(key, seq));
    }

    @Transactional
    public void participate(Long auctionSeq) {
        joinTemplate(auctionSeq, (key, seq) -> redisTemplate.opsForZSet().add(key, seq, System.currentTimeMillis()));
    }

    private void joinTemplate(Long auctionSeq, ObjLongConsumer<String> consumer) {
        User loggedInUser = userCommonService.getLoggedInUser();
        Auction auction = auctionRepository.findBySequenceAndIsUseIsTrueAndStatus(auctionSeq, AuctionStatus.RUNNING).orElseThrow(() -> new AuctionApiException(AuctionErrorCode.SEQ_NOT_FOUND_OR_NOT_RUNNING));

        String redisKey = String.format("auction:%d:participant", auctionSeq);
        String redisChannel = String.format("/sub/auction/%d/participant", auctionSeq);

        if(getCurrentCount(redisKey) >= auction.getMaxParticipantCount()) {
            throw new AuctionApiException(OVER_MAX_PARTICIPANT_COUNT);
        }

        consumer.accept(redisKey, loggedInUser.getSequence());

        AuctionParticipantDto.AuctionParticipantMessage message = AuctionParticipantDto.AuctionParticipantMessage.builder()
                .auctionSeq(auctionSeq)
                .userSeq(loggedInUser.getSequence())
                .count(getCurrentCount(redisKey))
                .build();

        redisTemplate.convertAndSend(redisChannel, AuctionParticipantDto.create(message));
    }

    private Long getCurrentCount(String redisKey) {
        return redisTemplate.opsForZSet().zCard(redisKey);
    }
}
