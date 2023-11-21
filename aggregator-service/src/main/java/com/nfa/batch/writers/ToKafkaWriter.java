package com.nfa.batch.writers;

import com.nfa.entities.Article;
import com.nfa.entities.Keyword;
import com.nfa.repositories.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ToKafkaWriter implements ItemWriter<Article> {

    private static final String KEYWORDS_TOPIC = "keywordstopic";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public void write(Chunk<? extends Article> chunk) {
        log.info("Producing ={}= keywords to Kafka", chunk.getItems().size());
        Set<String> uniqueKeywords = chunk.getItems().stream()
                .map(Article::getKeywords)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .map(Keyword::getName)
                .collect(Collectors.toSet());

        uniqueKeywords.forEach(this::produce);

        chunk.getItems().forEach(this::markProcessed);
    }

    private void produce(String message) {
        log.info("Producing keyword ={}= to Kafka", message);
        kafkaTemplate.send(KEYWORDS_TOPIC, message);
    }

    private void markProcessed(Article article) {
        article.setProcessed(true);
        articleRepository.save(article);
    }


}
