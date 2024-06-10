package com.kmhoon.batch.schedule.auction;

import com.kmhoon.batch.jobBean.DefaultScheduleJobBean;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.TimeZone;

@Component
@RequiredArgsConstructor
public class AuctionStatusJobSchedule {

    private final JobLauncher jobLauncher;
    private final JobLocator jobLocator;

    public JobDetail auctionStatusJobDetail() {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobName", "auctionStatusJob");
        jobDataMap.put("jobLauncher", jobLauncher);
        jobDataMap.put("jobLocator", jobLocator);

        return JobBuilder.newJob(DefaultScheduleJobBean.class)
                .withIdentity("auctionStatusJob")
                .setJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    public Trigger auctionStatusJobTrigger() {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 * * * * ?")
                .inTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Seoul")));

        return TriggerBuilder.newTrigger()
                .forJob(auctionStatusJobDetail())
                .withIdentity("auctionStatusJobTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }
}
