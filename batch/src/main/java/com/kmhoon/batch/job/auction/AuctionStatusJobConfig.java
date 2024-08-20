package com.kmhoon.batch.job.auction;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class AuctionStatusJobConfig {

    @Bean
    public Job auctionStatusJob(JobRepository jobRepository, Step startAuctionStep, Step endAuctionStep) {
        return new JobBuilder("auctionStatusJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(startAuctionStep)
                .next(endAuctionStep)
                .build();
    }

    @Bean
    public Step startAuctionStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, AuctionStartTasklet tasklet) {
        return new StepBuilder("startAuctionStep", jobRepository)
                .tasklet(tasklet, transactionManager)
                .build();
    }

    @Bean
    public Step endAuctionStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, AuctionEndTasklet tasklet) {
        return new StepBuilder("endAuctionStep", jobRepository)
                .tasklet(tasklet, transactionManager)
                .build();
    }

}
