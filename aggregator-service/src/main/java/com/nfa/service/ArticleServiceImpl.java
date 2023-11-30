package com.nfa.service;

import com.nfa.client.ReaderClient;
import com.nfa.controller.JwtRequest;
import com.nfa.dto.ArticleDto;
import com.nfa.dto.KeywordDto;
import com.nfa.dto.SourceDto;
import com.nfa.dto.SubscriptionDto;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private static final String DATE_ADDED = "dateAdded";
    private final ArticleRepository articleRepository;
    private final ReaderClient readerClient;
    private final KeywordService keywordService;

    @Override
    public List<ArticleDto> findAll(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize,
                Sort.by(DATE_ADDED).descending());

        Page<Article> articleEntities = articleRepository.findAll(pageRequest);

        return toArticleDtoList(articleEntities);
    }

    @Override
    public Optional<Article> findById(int theId) {
        return articleRepository.findById(theId);
    }

    @Override
    public Article save(ArticleDto articleDto) {
        return articleRepository.save(toEntity(articleDto));
    }

    @Override
    @Transactional
    public List<Article> saveAll(List<ArticleDto> articleDtoList) {
        return articleRepository.saveAll(
                articleDtoList.stream()
                        .map(this::toEntity)
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

    @Override
    public Set<ArticleDto> findAllByJwt (JwtRequest jwtRequest) {
        SubscriptionDto subscriptionDto = readerClient.getSubscriptionByJwt(jwtRequest);

        Set<Article> foundArticles = new HashSet<>();
        for (String keywordName: subscriptionDto.getKeywordNames()) {
            Optional<Keyword> savedKeyword = keywordService.getByName(keywordName);
            if (savedKeyword.isPresent()) {
                List<Article> articles = articleRepository.findByKeywords(savedKeyword.get());
                foundArticles.addAll(articles);
            }
        }
        return foundArticles.stream()
                .map(this::toDto)
                .collect(toSet());
    }

    private List<ArticleDto> toArticleDtoList(Page<Article> articleEntities) {
        List<ArticleDto> articleDtoList = new ArrayList<>();
        for (Article article : articleEntities) {
            articleDtoList.add(toDto(article));
        }
        return articleDtoList;
    }

    private ArticleDto toDto(Article article) {
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

    private Article toEntity(ArticleDto articleDto) {
        Article article = new Article();
        article.setTitle(articleDto.getTitle());
        article.setDescription(articleDto.getDescription());
        article.setUrl(articleDto.getUrl());
        article.setDateAdded(articleDto.getDateAdded());
        if (articleDto.getKeywordDtos() != null) {
            article.setKeywords(
                    articleDto.getKeywordDtos().stream()
                            .map(this::categoryDtoToEntity)
                            .collect(toSet()));
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
