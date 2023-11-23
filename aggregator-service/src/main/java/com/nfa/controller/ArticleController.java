package com.nfa.controller;

import com.nfa.dto.ArticleDto;
import com.nfa.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/{pageNumber}/{pageSize}")
    public List<ArticleDto> getArticles(@PathVariable int pageNumber, @PathVariable int pageSize) {
        return articleService.findAll(pageNumber,pageSize);
    }

}
