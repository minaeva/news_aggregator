package com.nfa.repository;

import com.nfa.entity.Article;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

    @Override
    List<Article> findAll(@NonNull Sort sort);

    Optional<List<Article>> findArticleByDateAdded(LocalDateTime date);

    List<Article> findAllByProcessedIsFalseAndKeywordsNotEmpty();
}
