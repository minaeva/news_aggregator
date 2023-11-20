package com.nfa.batch.writers;

import com.nfa.entities.Article;
import com.nfa.repositories.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class ArticleWriter implements ItemWriter<Article> {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private ArticleRepository articleRepository;

    @Override
    public void write(Chunk<? extends Article> chunk) {
        log.info("Writing: {}", chunk.getItems().size());
        articleRepository.saveAll(chunk.getItems());
    }
}
