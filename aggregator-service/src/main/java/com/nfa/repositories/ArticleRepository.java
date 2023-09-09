package com.nfa.repositories;

import com.nfa.entities.Article;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

    @Override
    List<Article> findAll(Sort sort);
}
