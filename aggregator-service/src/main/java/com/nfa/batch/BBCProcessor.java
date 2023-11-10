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

import static com.nfa.batch.ReaderHelper.getStringNotLongerThan;

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
        article.setTitle(getStringNotLongerThan(bbcArticle.getTitle(), 400));
        article.setDescription(getStringNotLongerThan(bbcArticle.getDescription(), 800));
        article.setContent(getStringNotLongerThan(bbcArticle.getContent(), 800));

        article.setUrl(getStringNotLongerThan(bbcArticle.getUrl(), 400));
        article.setDateAdded(bbcArticle.getPublishedAt());

        Source source = sourceService.getByNameOrSave(bbcArticle.getSource().getName());
        article.setSource(source);

//        Category category = categoryService.getByNameOrSave(NOT_SPECIFIED);
//        article.setCategories(List.of(category));

        log.info("ARTICLE: {}", article);
        return article;
    }
}

