package com.nfa.service;

import com.nfa.client.ReaderClient;
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
    public Set<ArticleDto> findAllByJwt(String jwtWithBearer) {
        SubscriptionDto subscriptionDto = readerClient.getSubscriptionByJwt(jwtWithBearer);

        Set<Keyword> readersKeywords = new HashSet<>();
        for (String keywordName : subscriptionDto.keywordNames()) {
            Optional<Keyword> savedKeyword = keywordService.getByName(keywordName);
            savedKeyword.ifPresent(readersKeywords::add);
        }

        List<Article> articles = articleRepository.findByKeywordsIn(readersKeywords);
        return articles.stream()
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
        SourceDto sourceDto = null;
        if (article.getSource() != null) {
            sourceDto = new SourceDto(article.getSource().getName());
        }

        List<KeywordDto> keywordDtoList = new ArrayList<>();
        for (Keyword keyword : article.getKeywords()) {
            keywordDtoList.add(new KeywordDto(keyword.getName()));
        }

        return new ArticleDto(article.getTitle(), article.getDescription(), article.getUrl(),
                article.getDateAdded(), sourceDto, keywordDtoList);
    }

    private Article toEntity(ArticleDto articleDto) {
        Article article = new Article();
        article.setTitle(articleDto.title());
        article.setDescription(articleDto.description());
        article.setUrl(articleDto.url());
        article.setDateAdded(articleDto.dateAdded());
        if (articleDto.keywordDtos() != null) {
            article.setKeywords(
                    articleDto.keywordDtos().stream()
                            .map(this::toEntity)
                            .collect(toSet()));
        }
        return article;
    }

    private Keyword toEntity(KeywordDto keywordDto) {
        Keyword keyword = new Keyword();
        keyword.setName(keywordDto.name());
        return keyword;
    }
}
