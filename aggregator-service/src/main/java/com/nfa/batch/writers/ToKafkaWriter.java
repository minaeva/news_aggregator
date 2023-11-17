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

@Slf4j
@Service
public class ToKafkaWriter implements ItemWriter<Article> {

    public static final String NEWS_AGGREGATED = "news_aggregated";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    ArticleRepository articleRepository;

    @Override
    public void write(Chunk<? extends Article> chunk) {
        log.info("Producing keywords to Kafka: {}", chunk.getItems().size());
        chunk.getItems().stream()
                .map(Article::getKeywords)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .map(Keyword::getName)
                .forEach(this::produce);

        chunk.getItems().forEach(this::markProcessed);
    }

    private void produce(String message) {
        log.info("Producing keyword {} to Kafka", message);
        kafkaTemplate.send(NEWS_AGGREGATED, message);
    }

    private void markProcessed(Article article) {
        article.setProcessed(true);
        articleRepository.save(article);
    }


}
