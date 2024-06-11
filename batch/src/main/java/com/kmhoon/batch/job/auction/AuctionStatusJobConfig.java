package com.kmhoon.batch.job.auction;

import com.kmhoon.common.enums.AuctionStatus;
import com.kmhoon.common.model.entity.service.auction.Auction;
import com.kmhoon.common.repository.service.auction.AuctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AuctionStatusJobConfig {

    private final AuctionRepository auctionRepository;

    @Bean
    public Job auctionStatusJob(JobRepository jobRepository, Step startAuctionStep, Step endAuctionStep) {
        return new JobBuilder("auctionStatusJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(startAuctionStep)
                .next(endAuctionStep)
                .build();
    }

    @Bean
    public Step startAuctionStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("startAuctionStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    String date = chunkContext.getStepContext().getJobParameters().get("date").toString();
                    List<Auction> startTargetAuctionList = auctionRepository.findAllByStartTimeAndIsUseIsTrueAndStatus(LocalDateTime.parse(date).withSecond(0).withNano(0), AuctionStatus.BEFORE);
                    startTargetAuctionList.forEach(auction -> auction.updateAuctionStatus(AuctionStatus.RUNNING));
                    return RepeatStatus.FINISHED;
                    }, transactionManager)
                .build();
    }

    @Bean
    public Step endAuctionStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("endAuctionStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    String date = chunkContext.getStepContext().getJobParameters().get("date").toString();
                    List<Auction> startTargetAuctionList = auctionRepository.findAllByEndTimeAndIsUseIsTrueAndStatus(LocalDateTime.parse(date).withSecond(0).withNano(0), AuctionStatus.RUNNING);
                    startTargetAuctionList.forEach(auction -> auction.updateAuctionStatus(AuctionStatus.FINISHED));
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
