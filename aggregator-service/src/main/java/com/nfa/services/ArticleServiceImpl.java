package com.nfa.services;

import com.nfa.dto.ArticleDto;
import com.nfa.dto.CategoryDto;
import com.nfa.dto.SourceDto;
import com.nfa.entities.Article;
import com.nfa.entities.Category;
import com.nfa.repositories.ArticleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Transactional
    public List<Article> saveAll(List<ArticleDto> articleDtoList) {
        return articleRepository.saveAll(
                articleDtoList.stream()
                        .map(articleDto -> dtoToEntity(articleDto))
                        .toList());
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
            article.setCategories(
                    articleDto.getCategories().stream()
                            .map(categoryDto -> categoryDtoToEntity(categoryDto))
                            .collect(Collectors.toSet()));
        }
        return article;
    }

    private Category categoryDtoToEntity(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        return category;
    }
}
