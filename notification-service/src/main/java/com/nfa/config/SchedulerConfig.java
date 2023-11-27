package com.nfa.config;

import com.nfa.client.EmailClient;
import com.nfa.client.ReaderClient;
import com.nfa.service.KeywordService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Collection;
import java.util.Set;

@Configuration
@EnableScheduling
@Slf4j
@AllArgsConstructor
public class SchedulerConfig {

    private final KeywordService keywordService;
    private final ReaderClient readerClient;
    private final EmailClient emailClient;

    @Scheduled(cron = "${cron.expression}")
    public void handleKeywords() {
        log.info("***");
        log.info("Job scheduler started");

        Set<String> cachedKeywords = keywordService.findAll();
        cachedKeywords.stream()
                .map(keyword -> readerClient.getSubscriptionsByKeyword(keyword))
                .flatMap(Collection::stream)
                .forEach(subscriptionDto -> emailClient.sendEmail(subscriptionDto.getReaderEmail()));

        cachedKeywords.stream()
                .forEach(keyword -> keywordService.delete(keyword));

        log.info("***");
        log.info("Job scheduler finished");
    }

}

