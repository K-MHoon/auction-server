package com.kmhoon.batch.job.auction;

import com.kmhoon.common.enums.AuctionStatus;
import com.kmhoon.common.model.entity.auth.user.User;
import com.kmhoon.common.model.entity.service.auction.Auction;
import com.kmhoon.common.model.entity.service.inventory.Inventory;
import com.kmhoon.common.repository.auth.user.UserRepository;
import com.kmhoon.common.repository.service.auction.AuctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AuctionEndTasklet implements Tasklet {

    private final AuctionRepository auctionRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String date = chunkContext.getStepContext().getJobParameters().get("date").toString();
        List<Auction> startTargetAuctionList = auctionRepository.findAllByEndTimeAndIsUseIsTrueAndStatus(LocalDateTime.parse(date).withSecond(0).withNano(0), AuctionStatus.RUNNING);

        for (Auction auction : startTargetAuctionList) {
            String redisPriceKey = String.format("auction:%d:price", auction.getSequence());
            String redisParticipantKey = String.format("auction:%d:participant", auction.getSequence());
            ZSetOperations.TypedTuple<Object> highScoreTuple = getHighScoreTuple(redisPriceKey);
            if (highScoreTuple != null) {
                // 1. 낙찰자 검증
                Optional<User> buyerOps = userRepository.findByEmailWithInventory(highScoreTuple.getValue().toString());
                if (buyerOps.isEmpty()) {
                    continue;
                }
                User buyer = buyerOps.get();
                Inventory inventory = buyer.getInventory();
                if(inventory.getMoney() < highScoreTuple.getScore().longValue()) {
                    continue;
                }
                // 2. 낙찰가, 낙찰 최종 시간, 낙찰자 업데이트
                auction.updatePrice(highScoreTuple.getScore().longValue());
                auction.updateSoldTime(auction.getEndTime());
                auction.updateBuyer(buyer);

                // 3. 금액 차감
                inventory.updateMoney(inventory.getMoney() - auction.getPrice());

                // 4. 낙찰자의 인벤토리로 이동시킨다.
                auction.getItem().updateInventory(buyer.getInventory());
            }
            auction.updateAuctionStatus(AuctionStatus.FINISHED);

            redisTemplate.delete(List.of(redisPriceKey, redisParticipantKey));
        }
        return RepeatStatus.FINISHED;
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
