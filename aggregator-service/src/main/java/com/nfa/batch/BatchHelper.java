package com.nfa.batch;

import com.nfa.entities.Article;
import com.nfa.entities.Keyword;
import com.nfa.services.KeywordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class BatchHelper {

    @Autowired
    private KeywordService keywordService;

    public static String getStringNotLongerThan(String input, int length) {
        if (input.length() < length) {
            return input;
        } else {
            return input.substring(0, length - 1);
        }
    }

    public void setKeywordsTitleContains(Article article) {
        Set<Keyword> allKeywords = Set.copyOf(keywordService.findAll());
        Set<Keyword> articleKeywords = new HashSet<>();
        String articleTitle = article.getTitle();

        // todo articleTitle.split(" ")
        for (Keyword aKeyword : allKeywords) {
            if (articleTitle.toLowerCase().contains(aKeyword.getName().toLowerCase())) {
                articleKeywords.add(aKeyword);
            }
        }
        if (articleKeywords.isEmpty()) {
            article.setProcessed(true);
        } else {
            article.setKeywords(articleKeywords);
        }
        log.info("ARTICLE: {}", article);
    }

}
