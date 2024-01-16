package com.nfa.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.nfa.dto.ArticleDto;
import com.nfa.dto.KeywordDto;
import com.nfa.dto.SubscriptionDto;
import com.nfa.entity.primary.Article;
import com.nfa.entity.primary.Keyword;
import com.nfa.repository.primary.ArticleRepository;
import com.nfa.repository.primary.KeywordRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WireMockTest(httpPort = 8181)
@Testcontainers
public class ArticleServiceIntegrationTest {

    @Autowired
    private ArticleService subject;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private KeywordRepository keywordRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${test.datasource.primary.username}")
    private String primaryDbUsername;

    @Value("${test.datasource.primary.password}")
    private String primaryDbPassword;

    @Value("${test.datasource.secondary.username}")
    private String secondaryDbUsername;

    @Value("${test.datasource.secondary.password}")
    private String secondaryDbPassword;

    @Container
    public static PostgreSQLContainer<?> primaryPostgreSQLContainer =
            new PostgreSQLContainer<>("postgres:latest")
                    .withUsername("TO_OBSCURE")
                    .withPassword("TO_OBSCURE");

    @Container
    public static PostgreSQLContainer<?> secondaryPostgreSQLContainer =
            new PostgreSQLContainer<>("postgres:latest")
                    .withUsername("TO_OBSCURE")
                    .withPassword("TO_OBSCURE");

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", primaryPostgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", primaryPostgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", primaryPostgreSQLContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
        registry.add("spring.secondary-datasource.url", secondaryPostgreSQLContainer::getJdbcUrl);
        registry.add("spring.secondary-datasource.username", secondaryPostgreSQLContainer::getUsername);
        registry.add("spring.secondary-datasource.password", secondaryPostgreSQLContainer::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        primaryPostgreSQLContainer.start();
        secondaryPostgreSQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        primaryPostgreSQLContainer.stop();
        secondaryPostgreSQLContainer.stop();
    }

    @BeforeEach
    void setUp() {
        articleRepository.deleteAll();
    }

    @Test
    void isArticleInDB_whenNoArticleWithSameDate_shouldReturnFalse() {
        LocalDateTime time = LocalDateTime.now();

        boolean result = subject.isArticleInDB(time, "title");

        assertThat(result).isFalse();
    }

    @Test
    void isArticleInDB_whenArticleWithSameDateExists_andTitleIsDifferent_shouldReturnFalse() {
        LocalDateTime time = LocalDateTime.now();
        Article article = new Article();
        article.setDateCreated(time);
        article.setTitle("other");
        articleRepository.save(article);

        boolean result = subject.isArticleInDB(time, "title");

        assertThat(result).isFalse();
    }

    @Test
    void isArticleInDB_whenArticleWithSameDateExists_andTitleIsSame_shouldReturnTrue() {
        LocalDateTime time = LocalDateTime.now();
        Article article = new Article();
        article.setDateCreated(time);
        article.setTitle("title");
        articleRepository.save(article);

        boolean result = subject.isArticleInDB(time, "title");

        assertThat(result).isTrue();
    }

    @Test
    void findAllByJwt_whenSubscriptionDoesNotExist_shouldReturnEmptySet() {
        stubFor(WireMock.get(urlPathMatching("/subscription/jwt"))
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withJsonBody(null)));

        Set<ArticleDto> result = subject.findAllByJwt("token");

        assertThat(result).isEqualTo(Set.of());
    }

    @Test
    void findAllByJwt_whenSubscriptionExists_andKeywordDoesNotExist_shouldReturnEmptySet() {
        SubscriptionDto subscriptionDto = new SubscriptionDto(
                1L, "Alex", "alex@gmail.com", List.of("peace"), 1);
        JsonNode subscriptionAsJson = objectMapper.valueToTree(subscriptionDto);
        stubFor(WireMock.get(urlPathMatching("/subscription/jwt"))
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withJsonBody(subscriptionAsJson)));

        Set<ArticleDto> result = subject.findAllByJwt("token");

        assertThat(result).isEqualTo(Set.of());
    }

    @Test
    void findAllByJwt_whenSubscriptionExists_andArticleDoesNotExist_shouldReturnEmptySet() {
        SubscriptionDto subscriptionDto = new SubscriptionDto(
                1L, "Alex", "alex@gmail.com", List.of("peace"), 1);
        JsonNode subscriptionAsJson = objectMapper.valueToTree(subscriptionDto);
        stubFor(WireMock.get(urlPathMatching("/subscription/jwt"))
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withJsonBody(subscriptionAsJson)));
        Keyword keyword = new Keyword("climate");
        keywordRepository.save(keyword);

        Set<ArticleDto> result = subject.findAllByJwt("token");

        assertThat(result).isEqualTo(Set.of());
    }

    @Test
    void findAllByJwt_whenSubscriptionExists_andArticleExists_shouldReturnArticle() {
        SubscriptionDto subscriptionDto = new SubscriptionDto(
                1L, "Alex", "alex@gmail.com", List.of("peace"), 1);
        JsonNode subscriptionAsJson = objectMapper.valueToTree(subscriptionDto);
        stubFor(WireMock.get(urlPathMatching("/subscription/jwt"))
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withJsonBody(subscriptionAsJson)));
        Article foundArticle = new Article("title", "description", "content", "url");
        Keyword keyword = new Keyword("peace");
        foundArticle.setKeywords(Set.of(keyword));
        articleRepository.save(foundArticle);

        Set<ArticleDto> result = subject.findAllByJwt("token");

        Set<ArticleDto> articleDtos = Set.of(new ArticleDto("title", "description", "content",
                "url", null, null, List.of(new KeywordDto("peace"))));
        assertThat(articleDtos)
                .usingRecursiveAssertion()
                .ignoringFields("id", "processed")
                .isEqualTo(result);
    }
}
