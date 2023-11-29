package com.nfa.scheduler;

import com.nfa.service.EmailService;
import com.nfa.client.ReaderClient;
import com.nfa.dto.SubscriptionDto;
import com.nfa.service.KeywordService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@EnableScheduling
@Slf4j
@AllArgsConstructor
public class SchedulerComponent {

    private final KeywordService keywordService;
    private final ReaderClient readerClient;
    private final EmailService emailService;

    @Scheduled(cron = "${cron.expression}")
    public void handleKeywords() {
        log.info("Job started");

        Set<String> cachedKeywords = keywordService.findAll();
        Set<String> emails = cachedKeywords.stream()
                .map(readerClient::getSubscriptionsByKeyword)
                .flatMap(Collection::stream)
                .map(SubscriptionDto::getReaderEmail)
                .collect(Collectors.toSet());

        if (!emails.isEmpty()) {
            log.info("Processing emails: {}", emails);
            emailService.sendEmailAsync(emails);
            cachedKeywords.forEach(keywordService::delete);
        }

        log.info("Job finished");
    }

}

