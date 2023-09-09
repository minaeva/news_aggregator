package com.nfa.controllers;

import com.nfa.dto.ArticleDto;
import com.nfa.dto.SourceDto;
import com.nfa.services.ArticleService;
import com.nfa.services.SourceService;
import jakarta.annotation.PostConstruct;
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
    private final SourceService sourceService;

    @GetMapping("/{pageNumber}/{pageSize}")
    public List<ArticleDto> getArticles(@PathVariable int pageNumber, @PathVariable int pageSize) {
        return articleService.findAll(pageNumber,pageSize);
    }

    @PostConstruct
    void prepareData() {
        SourceDto sourceDto = new SourceDto("bbc");
        sourceService.save(sourceDto);

        ArticleDto article1 = new ArticleDto("title1","description1", "url1", sourceDto);
        ArticleDto article2 = new ArticleDto("title2","description2", "url2", sourceDto);
        articleService.save(article1);
        articleService.save(article2);
    }
}
