package com.nfa.batch;


import com.nfa.client.responses.GnewsArticle;
import com.nfa.entities.Article;
import com.nfa.entities.Source;
import com.nfa.services.CategoryService;
import com.nfa.services.SourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

@Slf4j
public class GnewsProcessor implements ItemProcessor<GnewsArticle, Article> {

    private static final String NOT_SPECIFIED = "NOT_SPECIFIED";

    @Autowired
    private SourceService sourceService;

    @Autowired
    private CategoryService categoryService;

    @Override
    public Article process(@NonNull GnewsArticle gnewsArticle) throws Exception {
        log.info("Processing Gnews article for {}", gnewsArticle);


        Article article = new Article();
        article.setTitle(gnewsArticle.getTitle());
        article.setDescription(gnewsArticle.getDescription());
        article.setContent(gnewsArticle.getContent());


        article.setUrl(gnewsArticle.getUrl());
        article.setDateAdded(gnewsArticle.getPublishedAt());


        Source source = sourceService.getByNameOrSave(gnewsArticle.getSource().getName());
        article.setSource(source);

//        Category category = categoryService.getByNameOrSave(NOT_SPECIFIED);
//        article.setCategories(List.of(category));

        log.info("ARTICLE: {}", article);
        return article;
    }
}

