package com.nfa.batch.processors;

import com.nfa.client.responses.NYTArticle;
import com.nfa.entities.Article;
import com.nfa.entities.Keyword;
import com.nfa.entities.Source;
import com.nfa.services.ArticleService;
import com.nfa.services.KeywordService;
import com.nfa.services.SourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

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
    private KeywordService keywordService;

    @Override
    public Article process(@NonNull NYTArticle nytArticle) {
        log.info("Processing NYT article for {}", nytArticle);

        if (articleService.isArticleInDB(nytArticle.getPublishedDate(), nytArticle.getTitle())) {
            return null;
        }

        Article article = new Article();
        article.setTitle(getStringNotLongerThan(nytArticle.getTitle(), 400));
        article.setContent(getStringNotLongerThan(nytArticle.getContent(), 800));

        article.setUrl(getStringNotLongerThan(nytArticle.getUrl(), 400));
        article.setDateAdded(nytArticle.getPublishedDate());

        Source source = sourceService.getByNameOrSave(nytArticle.getSource());
        article.setSource(source);

        Set<Keyword> allKeywords = Set.copyOf(keywordService.findAll());
        String articleTitle = article.getTitle();

        Set<Keyword> articleKeywords = new HashSet<>();
        // todo articleTitle.split(" ")
        for (Keyword aKeyword : allKeywords) {
            if (articleTitle.toLowerCase().contains(aKeyword.getName().toLowerCase())) {
                articleKeywords.add(aKeyword);
            }
        }
        article.setKeywords(articleKeywords);

        log.info("ARTICLE: {}", article);
        return article;
    }
}

