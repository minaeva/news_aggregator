package com.nfa.services;

import com.nfa.dto.ArticleDto;
import com.nfa.entities.Article;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ArticleService {

    List<ArticleDto> findAll(int pageNumber, int pageSize);

    Optional<Article> findById(int theId);

    Article save(ArticleDto articleDto);

    List<Article> saveAll(List<ArticleDto> articleDtoList);

    void deleteById(int theId);

    boolean isArticleInDB(LocalDateTime dateAdded, String title);

}
