package com.nfa.batch.readers;

import com.nfa.entities.Article;
import com.nfa.repositories.ArticleRepository;
import org.springframework.batch.item.ItemReader;

import java.util.Iterator;
import java.util.List;

public class ArticleFromDbItemReader implements ItemReader<Article> {

    ArticleRepository articleRepository;
    private Iterator<Article> dataIterator;

    public ArticleFromDbItemReader(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public Article read() {
        if (dataIterator == null) {
            dataIterator = fetchDataFromDb().listIterator();
        }
        return dataIterator.hasNext() ? dataIterator.next() : null;
    }

    private List<Article> fetchDataFromDb() {
        return articleRepository.findAllByProcessedIsFalseAndKeywordsNotEmpty();
    }
}
