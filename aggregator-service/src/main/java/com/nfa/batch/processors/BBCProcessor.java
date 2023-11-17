package com.nfa.batch.processors;

import com.nfa.client.responses.BBCArticle;
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
public class BBCProcessor implements ItemProcessor<BBCArticle, Article> {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private SourceService sourceService;

    @Autowired
    private KeywordService keywordService;

    @Override
    public Article process(@NonNull BBCArticle bbcArticle) {
        log.info("Processing BBC article for {}", bbcArticle);

        if (articleService.isArticleInDB(bbcArticle.getPublishedAt(), bbcArticle.getTitle())) {
            return null;
        }

        Article article = new Article();
        article.setTitle(getStringNotLongerThan(bbcArticle.getTitle(), 400));
        article.setDescription(getStringNotLongerThan(bbcArticle.getDescription(), 800));
        article.setContent(getStringNotLongerThan(bbcArticle.getContent(), 800));

        article.setUrl(getStringNotLongerThan(bbcArticle.getUrl(), 400));
        article.setDateAdded(bbcArticle.getPublishedAt());

        Source source = sourceService.getByNameOrSave(bbcArticle.getSource().getName());
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

