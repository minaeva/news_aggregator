package com.nfa.batch.processors;


import com.nfa.batch.BatchHelper;
import com.nfa.client.responses.GnewsArticle;
import com.nfa.entities.Article;
import com.nfa.entities.Source;
import com.nfa.services.ArticleService;
import com.nfa.services.SourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import static com.nfa.batch.BatchHelper.getStringNotLongerThan;

@Slf4j
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class GnewsProcessor implements ItemProcessor<GnewsArticle, Article> {

    @Autowired
    private SourceService sourceService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private BatchHelper batchHelper;

    @Override
    public Article process(@NonNull GnewsArticle gnewsArticle) {
        log.info("Processing Gnews article for {}", gnewsArticle);

        if (articleService.isArticleInDB(gnewsArticle.getPublishedAt(), gnewsArticle.getTitle())) {
            return null;
        }

        Article article = new Article();
        article.setTitle(getStringNotLongerThan(gnewsArticle.getTitle(), 400));
        article.setDescription(getStringNotLongerThan(gnewsArticle.getDescription(), 800));
        article.setContent(getStringNotLongerThan(gnewsArticle.getContent(), 800));
        article.setUrl(getStringNotLongerThan(gnewsArticle.getUrl(), 400));
        article.setDateAdded(gnewsArticle.getPublishedAt());

        Source source = sourceService.getByNameOrSave(gnewsArticle.getSource().getName());
        article.setSource(source);

        batchHelper.setKeywordsTitleContains(article);
        return article;
    }
}

