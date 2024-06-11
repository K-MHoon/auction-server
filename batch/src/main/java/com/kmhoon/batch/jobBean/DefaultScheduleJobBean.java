package com.kmhoon.batch.jobBean;

import lombok.Getter;
import lombok.Setter;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class DefaultScheduleJobBean extends QuartzJobBean {

    private String jobName;
    private JobLocator jobLocator;
    private JobLauncher jobLauncher;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            Job job = this.jobLocator.getJob(this.jobName);
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("id", String.valueOf(System.currentTimeMillis()))
                    .addString("date", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                    .toJobParameters();
            this.jobLauncher.run(job, jobParameters);
        } catch (Exception e) {
            throw new JobExecutionException("Job 실행 에러가 발생했습니다.", e);
        }
    }
}
