package com.kmhoon.batch.config;

import com.kmhoon.batch.schedule.auction.AuctionStatusJobSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
@RequiredArgsConstructor
public class QuartzConfig {

    private final AuctionStatusJobSchedule auctionStatusJobSchedule;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
        scheduler.setTriggers(auctionStatusJobSchedule.auctionStatusJobTrigger());
        scheduler.setJobDetails(auctionStatusJobSchedule.auctionStatusJobDetail());
        return scheduler;
    }
}
