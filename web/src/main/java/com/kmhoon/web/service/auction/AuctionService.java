package com.kmhoon.web.service.auction;

import com.kmhoon.common.enums.AuctionStatus;
import com.kmhoon.common.enums.CouponStatus;
import com.kmhoon.common.enums.ItemType;
import com.kmhoon.common.model.dto.service.auction.AuctionDto;
import com.kmhoon.common.model.entity.auth.user.User;
import com.kmhoon.common.model.entity.service.auction.Auction;
import com.kmhoon.common.model.entity.service.inventory.Coupon;
import com.kmhoon.common.model.entity.service.inventory.Inventory;
import com.kmhoon.common.model.entity.service.item.Item;
import com.kmhoon.common.repository.auth.user.UserRepository;
import com.kmhoon.common.repository.service.auction.AuctionRepository;
import com.kmhoon.common.repository.service.inventory.CouponRepository;
import com.kmhoon.common.repository.service.item.ItemRepository;
import com.kmhoon.common.utils.DateTimeUtil;
import com.kmhoon.web.exception.AuctionApiException;
import com.kmhoon.web.exception.code.auction.AuctionErrorCode;
import com.kmhoon.web.exception.code.service.item.ItemErrorCode;
import com.kmhoon.web.service.dto.PageResponseDto;
import com.kmhoon.web.service.dto.auction.request.AuctionServiceRequestDto;
import com.kmhoon.web.service.user.UserCommonService;
import com.kmhoon.web.socket.dto.AuctionParticipantDto;
import com.kmhoon.web.socket.dto.AuctionPriceDto;
import com.kmhoon.web.socket.dto.AuctionPriceUserHistoryDto;
import com.kmhoon.web.socket.dto.AuctionStatusDto;
import com.kmhoon.web.utils.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public void createAuction(AuctionServiceRequestDto.CreateAuction request) {
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
    public PageResponseDto<AuctionDto> getMyAuctionList(AuctionServiceRequestDto.GetMyAuctionList request) {
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

    @Transactional(readOnly = true)
    public long getCurrentPrice(Long auctionSeq) {
        Auction auction = auctionRepository.findBySequenceAndIsUseIsTrueAndStatus(auctionSeq, AuctionStatus.RUNNING).orElseThrow(() -> new AuctionApiException(AuctionErrorCode.SEQ_NOT_FOUND_OR_NOT_RUNNING));

        String redisKey = String.format("auction:%d:price", auction.getSequence());

        ZSetOperations.TypedTuple<Object> highScoreTuple = getHighScoreTuple(redisKey);
        return highScoreTuple == null ? auction.getMinPrice() : highScoreTuple.getScore().longValue();
    }

    @Transactional
    public void updatePrice(Long auctionSeq, Long price) {
        Auction auction = auctionRepository.findBySequenceAndIsUseIsTrueAndStatus(auctionSeq, AuctionStatus.RUNNING).orElseThrow(() -> new AuctionApiException(AuctionErrorCode.SEQ_NOT_FOUND_OR_NOT_RUNNING));
        User loggedInUser = userCommonService.getLoggedInUser();

        if(price > loggedInUser.getInventory().getMoney()) {
            throw new AuctionApiException(PRICE_LESS_THAN_HAS_MONEY);
        }

        String redisKey = String.format("auction:%d:price", auction.getSequence());
        String redisPriceHistoryKey = String.format("auction:%d:price:user:%d:history", auction.getSequence(), loggedInUser.getSequence());

        // 경매 참여자 공통으로 보일 Key
        String redisChannel = String.format("/sub/auction/%d/price", auction.getSequence());

        // 개인 화면에 보일 금액 작성 이력 확인
        String redisHistoryChannel = String.format("/sub/auction/%d/price/user/%d/history", auction.getSequence(), loggedInUser.getSequence());

        ZSetOperations.TypedTuple<Object> highScoreTuple = getHighScoreTuple(redisKey);

        long currentPrice = highScoreTuple == null ? auction.getMinPrice() : highScoreTuple.getScore().longValue();
        if(price <= currentPrice) {
            throw new AuctionApiException(PRICE_LESS_THAN_BEFORE_PRICE);
        }

        if(price < (currentPrice + auction.getPriceUnit())) {
            throw new AuctionApiException(PRICE_LESS_THAN_NEXT_PRICE);
        }

        redisTemplate.opsForZSet().add(redisKey, loggedInUser.getSequence(), price);
        redisTemplate.opsForZSet().add(redisPriceHistoryKey,price,System.currentTimeMillis());

        AuctionPriceDto.AuctionPriceMessage message = AuctionPriceDto.AuctionPriceMessage.builder()
                .auctionSeq(auctionSeq)
                .userSeq(loggedInUser.getSequence())
                .price(price)
                .build();

        redisTemplate.convertAndSend(redisChannel, AuctionPriceDto.create(message));
        sendUserPriceHistory(auction.getSequence(), loggedInUser.getSequence());
    }

    @Transactional(readOnly = true)
    public void sendUserPriceHistory(Long auctionSeq, Long userSeq) {
        Auction auction = auctionRepository.findBySequenceAndIsUseIsTrueAndStatus(auctionSeq, AuctionStatus.RUNNING).orElseThrow(() -> new AuctionApiException(AuctionErrorCode.SEQ_NOT_FOUND_OR_NOT_RUNNING));
        User loggedInUser = userCommonService.getLoggedInUser();

        if(!userSeq.equals(loggedInUser.getSequence())) {
            throw new AuctionApiException(NOT_MATCH_SUBSCRIBE_USER);
        }

        String redisPriceHistoryKey = String.format("auction:%d:price:user:%d:history", auction.getSequence(), loggedInUser.getSequence());
        String redisHistoryChannel = String.format("/sub/auction/%d/price/user/%d/history", auction.getSequence(), loggedInUser.getSequence());

        Set<ZSetOperations.TypedTuple<Object>> tuples = redisTemplate.opsForZSet().reverseRangeWithScores(redisPriceHistoryKey, 0, -1);
        List<AuctionPriceUserHistoryDto.PriceHistory> list = tuples.stream().map((tuple) -> {
            Long value = Long.valueOf((String) tuple.getValue());
            long score = tuple.getScore().longValue();
            Instant instant = Instant.ofEpochMilli(score);
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Seoul"));

            return AuctionPriceUserHistoryDto.PriceHistory.builder()
                    .price(value)
                    .date(DateTimeUtil.dateTimeToString(localDateTime))
                    .build();
        }).toList();

        AuctionPriceUserHistoryDto.AuctionPriceUserHistoryMessage historyMessage = AuctionPriceUserHistoryDto.AuctionPriceUserHistoryMessage.builder()
                .auctionSeq(auctionSeq)
                .userSeq(loggedInUser.getSequence())
                .priceHistoryList(list)
                .build();

        redisTemplate.convertAndSend(redisHistoryChannel, AuctionPriceUserHistoryDto.create(historyMessage));
    }


    @Transactional
    public void finishAuction(Long auctionSeq, AuctionStatus auctionStatus) {
        User loggedInUser = userCommonService.getLoggedInUser();
        Auction auction = auctionRepository.findBySequenceAndIsUseIsTrueAndStatusAndSeller(auctionSeq, AuctionStatus.RUNNING, loggedInUser).orElseThrow(() -> new AuctionApiException(AUCTION_NOT_FOUND));

        String redisPriceKey = String.format("auction:%d:price", auction.getSequence());
        String redisParticipantKey = String.format("auction:%d:participant", auction.getSequence());

        if(auctionStatus == AuctionStatus.STOPPED) {
            auction.updateAuctionStatus(AuctionStatus.STOPPED);
        } else if(auctionStatus == AuctionStatus.FINISHED) {
            auction.updateAuctionStatus(AuctionStatus.FINISHED);

            ZSetOperations.TypedTuple<Object> highScoreTuple = getHighScoreTuple(redisPriceKey);

            if (highScoreTuple != null) {
                // 1. 낙찰자 검증
                Optional<User> buyerOps = userRepository.findByEmailWithInventory(highScoreTuple.getValue().toString());

                if(buyerOps.isPresent()) {
                    User buyer = buyerOps.get();
                    Inventory buyerInventory = buyer.getInventory();

                    if(buyerInventory.getMoney() >= highScoreTuple.getScore().longValue()) {
                        auction.updatePrice(highScoreTuple.getScore().longValue());
                        auction.updateSoldTime(LocalDateTime.now());
                        auction.updateBuyer(buyer);
                        buyerInventory.updateMoney(buyerInventory.getMoney() - auction.getPrice());
                        auction.getItem().updateInventory(buyer.getInventory());
                    }
                }
            }
            auction.updateAuctionStatus(AuctionStatus.FINISHED);
        }

        redisTemplate.delete(List.of(redisPriceKey, redisParticipantKey));
        sendAuctionStatus(auctionSeq, auctionStatus);
    }

    /**
     * 경매방 참여자들에게 현재 경매 상태를 전달한다.
     *
     * @param auctionSeq
     * @param auctionStatus
     */
    private void sendAuctionStatus(Long auctionSeq, AuctionStatus auctionStatus) {
        String redisChannel = String.format("/sub/auction/%d/status", auctionSeq);

        AuctionStatusDto.AuctionStatusMessage message = AuctionStatusDto.AuctionStatusMessage.builder()
                .auctionSeq(auctionSeq)
                .auctionStatus(auctionStatus)
                .build();

        redisTemplate.convertAndSend(redisChannel, AuctionStatusDto.create(message));
    }

    private ZSetOperations.TypedTuple<Object> getHighScoreTuple(String key) {
        ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<Object>> resultSet = zSetOps.reverseRangeWithScores(key, 0, 0);

        if (resultSet != null && !resultSet.isEmpty()) {
            return resultSet.iterator().next();
        }
        return null;
    }
}
