package com.nfa.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Slf4j
@Configuration
@RequiredArgsConstructor
public class SchedulerConfig {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Scheduled(fixedDelayString = "${scheduled.delay.fixed}", initialDelayString = "${scheduled.delay.initial}")
    public void scheduleJob() throws Exception {
        log.info("***");
        log.info("Job scheduler started");
        jobLauncher.run(job, new JobParametersBuilder()
                .addLong("uniqueness", System.nanoTime()).toJobParameters());
        log.info("***");
        log.info("Job scheduler finished");
    }
}
