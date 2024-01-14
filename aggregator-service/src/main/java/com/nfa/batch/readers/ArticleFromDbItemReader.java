package com.nfa.batch.readers;

import com.nfa.entity.primary.Article;
import com.nfa.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;

import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class ArticleFromDbItemReader implements ItemReader<Article> {

    private final ArticleRepository articleRepository;
    private Iterator<Article> dataIterator;

    @Override
    public Article read() {
        if (dataIterator == null) {
            dataIterator = fetchDataFromDb().listIterator();
        }
        return dataIterator.hasNext() ? dataIterator.next() : null;
    }

    private List<Article> fetchDataFromDb() {
        List<Article> articlesToProcess = articleRepository.findAllByProcessedIsFalseAndKeywordsNotEmpty();
        log.info("fetchDataFromDb, articlesToProcess = {}", articlesToProcess);
        return articlesToProcess;
    }
}
