package com.nfa.config;

import com.nfa.model.Keyword;
import com.nfa.service.KeywordService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@EnableScheduling
@Slf4j
@AllArgsConstructor
public class SchedulerConfig {

    private final KeywordService keywordService;

    @Scheduled(cron = "${cron.expression}")
    public void sendEmails() {
        log.info("***");
        log.info("Job scheduler started");
        List<Keyword> cachedKeywords = keywordService.findAll();
//        cachedKeywords.forEach(getReaders
//                filter those who need email now
//                send emails
//                remove keywords from cache
//        )

        log.info("***");
        log.info("Job scheduler finished");
    }

}

