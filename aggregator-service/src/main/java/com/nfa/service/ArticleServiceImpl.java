package com.nfa.service;

import com.nfa.client.UserClient;
import com.nfa.dto.ArticleDto;
import com.nfa.dto.KeywordDto;
import com.nfa.dto.SourceDto;
import com.nfa.dto.SubscriptionDto;
import com.nfa.entity.primary.Article;
import com.nfa.entity.primary.Keyword;
import com.nfa.repository.primary.ArticleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    private static final String DATE_CREATED = "dateCreated";
    private final ArticleRepository articleRepository;
    private final UserClient userClient;
    private final KeywordService keywordService;

    @Override
    public List<ArticleDto> findAll(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize,
                Sort.by(DATE_CREATED).descending());

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
        List<Article> articlesByDateAdded = articleRepository.findArticleByDateCreated(dateAdded);
        return articlesByDateAdded.stream()
                .anyMatch(article -> article.getTitle().contains(title));
    }

    @Override
    @Transactional
    public List<ArticleDto> findAllByJwt(String jwtWithBearer) {
        SubscriptionDto subscriptionDto = userClient.getSubscriptionByJwt(jwtWithBearer);
        log.info("subscriptionDto is " + subscriptionDto);
        if (subscriptionDto == null) {
            log.info("Reader doesn't have any subscription with keywords to find the news to fit");
            return List.of();
        }

        Set<Keyword> readersKeywords = new HashSet<>();
        for (String keywordName : subscriptionDto.keywordNames()) {
            Optional<Keyword> savedKeyword = keywordService.getByName(keywordName);
            savedKeyword.ifPresent(readersKeywords::add);
        }
        log.info("readersKeywords " + readersKeywords);

        List<Article> articles = articleRepository.findByKeywordsIn(readersKeywords);

        ArticleDateComparator comparator = new ArticleDateComparator();
        articles.sort(comparator);
        return articles.stream()
                .distinct()
                .map(this::toDto)
                .toList();
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

        List<KeywordDto> keywordDtoList = article.getKeywords().stream()
                .map(Keyword::getName)
                .map(KeywordDto::new)
                .toList();

        return new ArticleDto(article.getTitle(), article.getDescription(), article.getContent(), article.getUrl(),
                article.getDateCreated(), sourceDto, keywordDtoList);
    }

    private Article toEntity(ArticleDto articleDto) {
        Article article = new Article();
        article.setTitle(articleDto.title());
        article.setDescription(articleDto.description());
        article.setUrl(articleDto.url());
        article.setDateCreated(articleDto.dateCreated());
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
