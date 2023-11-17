package com.nfa.batch.processors;

import com.nfa.batch.BatchHelper;
import com.nfa.client.responses.BBCArticle;
import com.nfa.entities.Article;
import com.nfa.entities.Source;
import com.nfa.services.ArticleService;
import com.nfa.services.SourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static com.nfa.batch.BatchHelper.getStringNotLongerThan;

@Slf4j
@Component
@StepScope
public class BBCProcessor implements ItemProcessor<BBCArticle, Article> {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private SourceService sourceService;

    @Autowired
    private BatchHelper batchHelper;

    @Override
    public Article process(@NonNull BBCArticle bbcArticle) {
        log.info("Processing BBC article for {}", bbcArticle);

        if (batchHelper.isNewArticle(bbcArticle.getPublishedAt(), bbcArticle.getTitle())) {
            return externalArticleToInternalArticle(bbcArticle);
        }
        return null;
    }

    private Article externalArticleToInternalArticle(BBCArticle bbcArticle) {
        Article article = new Article();
        article.setTitle(getStringNotLongerThan(bbcArticle.getTitle(), 400));
        article.setDescription(getStringNotLongerThan(bbcArticle.getDescription(), 800));
        article.setContent(getStringNotLongerThan(bbcArticle.getContent(), 800));
        article.setUrl(getStringNotLongerThan(bbcArticle.getUrl(), 400));
        article.setDateAdded(bbcArticle.getPublishedAt());

        Source source = sourceService.getByNameOrSave(bbcArticle.getSource().getName());
        article.setSource(source);

        batchHelper.setKeywordsTitleContains(article);
        return article;
    }
}

