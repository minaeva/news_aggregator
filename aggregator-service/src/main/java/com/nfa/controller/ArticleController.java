package com.nfa.controller;

import com.nfa.dto.ArticleDto;
import com.nfa.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articles")
@Slf4j
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/{pageNumber}/{pageSize}")
    public List<ArticleDto> getArticlesPaginated(@PathVariable int pageNumber, @PathVariable int pageSize) {
        log.info("getArticlesPaginated triggered ");
        return articleService.findAll(pageNumber, pageSize);
    }

    @GetMapping()
    public List<ArticleDto> getReadersArticles(@RequestHeader(AUTHORIZATION) String jwtWithBearer) {
        log.info("getReadersArticles triggered ");
        return articleService.findAllByJwt(jwtWithBearer);
    }

    private ResponseEntity<String> fallback(Throwable t) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Fallback Response");
    }
}
