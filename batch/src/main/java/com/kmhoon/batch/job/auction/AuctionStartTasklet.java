package com.kmhoon.batch.job.auction;

import com.kmhoon.common.enums.AuctionStatus;
import com.kmhoon.common.model.entity.service.auction.Auction;
import com.kmhoon.common.repository.service.auction.AuctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuctionStartTasklet implements Tasklet {

    private final AuctionRepository auctionRepository;

    @Override
    @Transactional
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String date = chunkContext.getStepContext().getJobParameters().get("date").toString();
        List<Auction> startTargetAuctionList = auctionRepository.findAllByStartTimeAndIsUseIsTrueAndStatus(LocalDateTime.parse(date).withSecond(0).withNano(0), AuctionStatus.BEFORE);
        startTargetAuctionList.forEach(auction -> auction.updateAuctionStatus(AuctionStatus.RUNNING));
        return RepeatStatus.FINISHED;
    }
}
