package com.nfa.services;

import com.nfa.dto.ArticleDto;
import com.nfa.dto.CategoryDto;
import com.nfa.dto.SourceDto;
import com.nfa.entities.Article;
import com.nfa.entities.Category;
import com.nfa.repositories.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private static final String DATE_ADDED = "dateAdded";
    private final ArticleRepository articleRepository;

    @Override
    public List<ArticleDto> findAll(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize,
                Sort.by(DATE_ADDED).descending());

        Page<Article> articleEntities = articleRepository.findAll(pageRequest);

        return entityToDtoArticleList(articleEntities);
    }

    @Override
    public Optional<Article> findById(int theId) {
        return articleRepository.findById(theId);
    }

    @Override
    public Article save(ArticleDto articleDto) {
        return articleRepository.save(dtoToEntity(articleDto));
    }

    @Override
    public void deleteById(int theId) {
        articleRepository.deleteById(theId);
    }

    private List<ArticleDto> entityToDtoArticleList(Page<Article> articleEntities) {
        List<ArticleDto> articleDtoList = new ArrayList<>();
        for (Article article : articleEntities) {
            articleDtoList.add(entityToDto(article));
        }
        return articleDtoList;
    }

    private ArticleDto entityToDto(Article article) {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle(article.getTitle());
        articleDto.setDescription(article.getDescription());
        articleDto.setUrl(article.getUrl());
        articleDto.setDateAdded(article.getDateAdded());
        if (article.getSource() != null) {
            articleDto.setSourceDto(new SourceDto(article.getSource().getName()));
        }
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        for (Category category : article.getCategories()) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setName(category.getName());
            categoryDtoList.add(categoryDto);
        }
        articleDto.setCategories(categoryDtoList);
        return articleDto;
    }

    private Article dtoToEntity(ArticleDto articleDto) {
        Article article = new Article();
        article.setTitle(articleDto.getTitle());
        article.setDescription(articleDto.getDescription());
        article.setUrl(articleDto.getUrl());
        article.setDateAdded(articleDto.getDateAdded());
        //todo article.setSource(articleDto.getSourceDto());
        if (articleDto.getCategories() != null) {
            article.setCategories(dtoToEntityCategoryList(articleDto));
        }
        return article;
    }

    private List<Category> dtoToEntityCategoryList(ArticleDto articleDto) {
        List<Category> categoryList = new ArrayList<>();
        for (CategoryDto categoryDto : articleDto.getCategories()) {
            Category category = new Category();
            category.setName(categoryDto.getName());
            categoryList.add(category);
        }
        return categoryList;
    }
}
