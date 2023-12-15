package com.nfa.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.nfa.dto.ArticleDto;
import com.nfa.dto.SubscriptionDto;
import com.nfa.entity.Article;
import com.nfa.entity.Keyword;
import com.nfa.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WireMockTest(httpPort = 8181)
public class ArticleServiceIntegrationTest {

    @Autowired
    private ArticleService subject;
    @MockBean
    private KeywordService keywordService;
    @MockBean
    private ArticleRepository articleRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void findAllByJwt_whenSubscriptionExists_andArticleExists_shouldReturnArticle() {
        SubscriptionDto subscriptionDto = new SubscriptionDto(
                1L, "Alex", "alex@gmail.com", List.of("peace"), 1);
        JsonNode subscriptionAsJson = objectMapper.valueToTree(subscriptionDto);
        stubFor(WireMock.get(urlPathMatching("/subscription/jwt"))
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withJsonBody(subscriptionAsJson)));

        Keyword keyword = new Keyword("peace");
        when(keywordService.getByName("peace")).thenReturn(Optional.of(keyword));

        Set<Keyword> set = Set.of(keyword);
        Article foundArticle = new Article("title", "description", "url");
        List<Article> articleList = List.of(foundArticle);
        when(articleRepository.findByKeywordsIn(set)).thenReturn(articleList);

        Set<ArticleDto> result = subject.findAllByJwt("token");

        Set<ArticleDto> articleDtos = Set.of(new ArticleDto("title", "description", "url",
                null, null, List.of()));
        assertThat(articleDtos)
                .usingRecursiveAssertion()
                .ignoringFields("id", "content", "processed")
                .isEqualTo(result);
    }

}
