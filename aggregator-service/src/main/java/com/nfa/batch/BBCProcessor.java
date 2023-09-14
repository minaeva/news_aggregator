package com.nfa.batch;

import com.nfa.client.responses.BBCArticle;
import com.nfa.entities.Article;
import com.nfa.entities.Source;
import com.nfa.services.CategoryService;
import com.nfa.services.SourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@StepScope
public class BBCProcessor implements ItemProcessor<BBCArticle, Article> {

    private static final String NOT_SPECIFIED = "NOT_SPECIFIED";

    @Autowired
    private SourceService sourceService;

    @Autowired
    private CategoryService categoryService;

    @Override
    public Article process(@NonNull BBCArticle bbcArticle) throws Exception {
        log.info("Processing BBC article for {}", bbcArticle);

        Article article = new Article();
        article.setTitle(bbcArticle.getTitle());
        article.setDescription(bbcArticle.getDescription());
        article.setContent(bbcArticle.getContent());

        article.setUrl(bbcArticle.getUrl());
        article.setDateAdded(bbcArticle.getPublishedAt());

        Source source = sourceService.getByNameOrSave(bbcArticle.getSource().getName());
        article.setSource(source);

//        Category category = categoryService.getByNameOrSave(NOT_SPECIFIED);
//        article.setCategories(List.of(category));

        log.info("ARTICLE: {}", article);
        return article;
    }
}

