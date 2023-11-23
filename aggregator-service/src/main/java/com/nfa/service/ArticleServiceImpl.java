package com.nfa.service;

import com.nfa.dto.ArticleDto;
import com.nfa.dto.KeywordDto;
import com.nfa.dto.SourceDto;
import com.nfa.entity.Article;
import com.nfa.entity.Keyword;
import com.nfa.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
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
                        .map(this::dtoToEntity)
                        .toList());
    }

    @Override
    public void deleteById(int theId) {
        articleRepository.deleteById(theId);
    }


    @Override
    public boolean isArticleInDB(LocalDateTime dateAdded, String title) {
        Optional<List<Article>> articlesByDateAdded = articleRepository.findArticleByDateAdded(dateAdded);
        AtomicBoolean result = new AtomicBoolean(false);
        articlesByDateAdded.ifPresent(articles ->
                result.set(articles.stream()
                        .anyMatch(article -> article.getTitle().contains(title))
                ));
        return result.get();
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
        List<KeywordDto> keywordDtoList = new ArrayList<>();
        for (Keyword keyword : article.getKeywords()) {
            KeywordDto keywordDto = new KeywordDto();
            keywordDto.setName(keyword.getName());
            keywordDtoList.add(keywordDto);
        }
        articleDto.setKeywordDtos(keywordDtoList);
        return articleDto;
    }

    private Article dtoToEntity(ArticleDto articleDto) {
        Article article = new Article();
        article.setTitle(articleDto.getTitle());
        article.setDescription(articleDto.getDescription());
        article.setUrl(articleDto.getUrl());
        article.setDateAdded(articleDto.getDateAdded());
        //TODO: article.setSource(articleDto.getSourceDto());
        if (articleDto.getKeywordDtos() != null) {
            article.setKeywords(
                    articleDto.getKeywordDtos().stream()
                            .map(this::categoryDtoToEntity)
                            .collect(Collectors.toSet()));
        }
        return article;
    }

    private Keyword categoryDtoToEntity(KeywordDto keywordDto) {
        Keyword keyword = new Keyword();
        keyword.setId(keywordDto.getId());
        keyword.setName(keywordDto.getName());
        return keyword;
    }
}
