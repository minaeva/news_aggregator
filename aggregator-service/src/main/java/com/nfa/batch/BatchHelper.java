package com.nfa.batch;

import com.nfa.entities.Article;
import com.nfa.entities.Keyword;
import com.nfa.services.ArticleService;
import com.nfa.services.KeywordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BatchHelper {

    @Autowired
    private KeywordService keywordService;

    @Autowired
    private ArticleService articleService;

    public static String getStringNotLongerThan(String input, int length) {
        if (input.length() < length) {
            return input;
        } else {
            return input.substring(0, length - 1);
        }
    }

    public boolean isNewArticle(LocalDateTime datePublished, String title) {
        return !articleService.isArticleInDB(datePublished, title);
    }

    public void setKeywordsTitleContains(Article article) {
        Set<Keyword> allKeywords = Set.copyOf(keywordService.findAll());
        //TODO: "Israel-Gaza live news: US warning as Israel storms Gaza's main hospital" should identify "gaza"
        List<String> titleWords = List.of(article.getTitle().toLowerCase().split(" "));

        Set<Keyword> articleKeywords = allKeywords.stream()
                .filter(element -> titleWords.contains(element.getName().toLowerCase()))
                .collect(Collectors.toSet());

        if (articleKeywords.isEmpty()) {
            article.setProcessed(true);
        } else {
            article.setKeywords(articleKeywords);
        }
        log.info("ARTICLE: {}", article);
    }
}
