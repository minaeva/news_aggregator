package com.nfa.repositories;

import com.nfa.entities.Article;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

    @Override
    List<Article> findAll(@NonNull Sort sort);

    Optional<List<Article>> findArticleByDateAdded(Date date);

    List<Article> findAllByProcessedIsFalseAndKeywordsNotEmpty();
}
