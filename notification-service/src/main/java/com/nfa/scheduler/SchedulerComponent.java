package com.nfa.scheduler;

import com.nfa.service.EmailService;
import com.nfa.client.UserClient;
import com.nfa.dto.SubscriptionDto;
import com.nfa.service.KeywordService;
import com.nfa.service.KeywordStatisticsService;
import com.nfa.service.ReaderStatisticsService;
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
    private final EmailService emailService;
    private final ReaderStatisticsService readerStatisticsService;
    private final KeywordStatisticsService keywordStatisticsService;
    private final UserClient userClient;

    @Scheduled(cron = "${cron.expression}")
    public void handleKeywords() {
        log.info("Job started");

        Set<String> cachedKeywords = keywordService.findAll();
        Set<String> emails = cachedKeywords.stream()
                .map(userClient::getSubscriptionsByKeyword)
                .flatMap(Collection::stream)
                .distinct()
                .peek(readerStatisticsService::save)
                .map(SubscriptionDto::readerEmail)
                .collect(Collectors.toSet());

        if (!emails.isEmpty()) {
            log.info("Processing emails: {}", emails);
            emailService.sendEmailAsync(emails);
            cachedKeywords.forEach(keywordStatisticsService::save);
            cachedKeywords.forEach(keywordService::delete);
        }

        log.info("Job finished");
    }

}

