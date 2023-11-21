package com.nfa.services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

    public static final String KEYWORDS_TOPIC = "keywordstopic";

    @KafkaListener(topics = KEYWORDS_TOPIC, groupId = "news_consumer_group_id")
    public void consume(String message) {
        System.out.println("Consumed " + message);
    }
}
