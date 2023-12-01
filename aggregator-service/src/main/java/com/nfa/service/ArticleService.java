package com.nfa.service;

import com.nfa.controller.JwtRequest;
import com.nfa.dto.ArticleDto;
import com.nfa.entity.Article;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ArticleService {

    List<ArticleDto> findAll(int pageNumber, int pageSize);

    Optional<Article> findById(int theId);

    Article save(ArticleDto articleDto);

    List<Article> saveAll(List<ArticleDto> articleDtoList);

    void deleteById(int theId);

    boolean isArticleInDB(LocalDateTime dateAdded, String title);

    Set<ArticleDto> findAllByJwt (String jwt);
}
