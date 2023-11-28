package com.nfa.config;

import com.nfa.service.EmailService;
import com.nfa.client.ReaderClient;
import com.nfa.dto.SubscriptionDto;
import com.nfa.service.KeywordService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
@Slf4j
@AllArgsConstructor
public class SchedulerConfig {

    private final KeywordService keywordService;
    private final ReaderClient readerClient;
    private final EmailService emailService;

    @Scheduled(cron = "${cron.expression}")
    public void handleKeywords() {
        log.info("***");
        log.info("Job scheduler started");

        Set<String> cachedKeywords = keywordService.findAll();
        Set<String> emails = cachedKeywords.stream()
                .map(keyword -> readerClient.getSubscriptionsByKeyword(keyword))
                .flatMap(Collection::stream)
                .map(SubscriptionDto::getReaderEmail)
                .collect(Collectors.toSet());

        if (!emails.isEmpty()) {
            log.info("Processing emails: ", emails);
            emailService.sendEmailAsync(emails);
            cachedKeywords.stream()
                    .forEach(keyword -> keywordService.delete(keyword));
        }

        log.info("***");
        log.info("Job scheduler finished");
    }

}

