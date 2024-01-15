package com.nfa.repository.primary;

import com.nfa.entity.primary.Article;
import com.nfa.entity.primary.Keyword;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

    @Override
    List<Article> findAll(@NonNull Sort sort);

    List<Article> findArticleByDateCreated(LocalDateTime date);

    List<Article> findAllByProcessedIsFalseAndKeywordsNotEmpty();

    List<Article> findByKeywordsIn(Set<Keyword> keywords);
}
