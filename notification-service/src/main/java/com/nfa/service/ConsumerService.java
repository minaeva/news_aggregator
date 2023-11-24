package com.nfa.service;

import com.nfa.model.Keyword;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ConsumerService {

    private final KeywordService keywordService;
    private static final String KEYWORDS_TOPIC = "keywordstopic";

    @KafkaListener(topics = KEYWORDS_TOPIC, groupId = "news_consumer_group_id")
    public void consume(String message) {
        log.info("Consumed " + message);

        if (keywordService.findByName(message).isEmpty()) {
            keywordService.save(new Keyword(message));
        }
    }
}
