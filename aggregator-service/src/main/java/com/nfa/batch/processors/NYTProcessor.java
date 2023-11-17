package com.nfa.batch.processors;

import com.nfa.batch.BatchHelper;
import com.nfa.client.responses.NYTArticle;
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
public class NYTProcessor implements ItemProcessor<NYTArticle, Article> {

    @Autowired
    private SourceService sourceService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private BatchHelper batchHelper;

    @Override
    public Article process(@NonNull NYTArticle nytArticle) {
        log.info("Processing NYT article for {}", nytArticle);
        if (batchHelper.isNewArticle(nytArticle.getPublishedDate().atStartOfDay(), nytArticle.getTitle())) {
            return externalArticleToInternalArticle(nytArticle);
        }
        return null;
    }

    private Article externalArticleToInternalArticle(NYTArticle nytArticle) {
        Article article = new Article();
        article.setTitle(getStringNotLongerThan(nytArticle.getTitle(), 400));
        article.setContent(getStringNotLongerThan(nytArticle.getContent(), 800));
        article.setUrl(getStringNotLongerThan(nytArticle.getUrl(), 400));
        article.setDateAdded(nytArticle.getPublishedDate().atStartOfDay());

        Source source = sourceService.getByNameOrSave(nytArticle.getSource());
        article.setSource(source);

        batchHelper.setKeywordsTitleContains(article);
        return article;
    }
}

