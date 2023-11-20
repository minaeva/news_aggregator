package com.nfa.batch;

import com.nfa.entities.Article;
import com.nfa.entities.Keyword;
import com.nfa.services.ArticleService;
import com.nfa.services.KeywordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
@Slf4j
public class BatchHelper {

    @Autowired
    private KeywordService keywordService;

    @Autowired
    private ArticleService articleService;

    private static final Set<String> WORDS_OF_NO_INTEREST = Set.of("in", "on", "at", "of", "about",
            "what", "why", "where", "who", "whatever", "while", "wherever", "other", "another", "here", "there",
            "to", "after", "before", "over", "under", "ago", "yet", "then", "for", "from", "still",
            "and", "as", "far", "have", "has", "had", "is", "are", "between", "a", "the", "-", "&");

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
        Set<String> distinctWords = Arrays.stream(article.getTitle().split(" ")).collect(toSet());
        Set<String> purifiedWords = purifyWords(distinctWords);
        Set<String> lowerCaseWords = purifiedWords.stream().map(String::toLowerCase).collect(toSet());

        Set<Keyword> allKeywords = Set.copyOf(keywordService.findAll());
        Set<Keyword> articleKeywords = allKeywords.stream()
                .filter(element -> lowerCaseWords.contains(element.getName().toLowerCase()))
                .collect(toSet());

        if (articleKeywords.isEmpty()) {
            article.setProcessed(true);
        } else {
            article.setKeywords(articleKeywords);
        }
        log.info("ARTICLE: {}", article);
    }

    private static Set<String> purifyWords(Set<String> words) {
        Set<String> result = new HashSet<>();
        for (String word : words) {
            if (word.endsWith("'s")) {
                result.add(word.substring(0, word.length() - 2));
            } else if (word.endsWith(".") || word.endsWith(",") || word.endsWith(":") || word.endsWith("!") || word.endsWith("?")) {
                result.add(word.substring(0, word.length() - 1));
            } else if (WORDS_OF_NO_INTEREST.contains(word.toLowerCase())) {
                continue;
            } else {
                result.add(word);
            }
        }
        return result;
    }
}
