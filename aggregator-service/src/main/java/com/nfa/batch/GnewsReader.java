package com.nfa.batch;

import com.nfa.batch.responses.GnewsArticle;
import com.nfa.batch.responses.GnewsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class GnewsReader implements ItemReader<GnewsArticle> {

    private final String url;
    private final RestTemplate restTemplate;
    private int nextArticle;
    private List<GnewsArticle> articleList;

    @Override
    public GnewsArticle read() throws Exception {
        if (this.articleList == null) {
            articleList = fetchArticles();
        }
        GnewsArticle article = null;

        if (nextArticle < articleList.size()) {
            article = articleList.get(nextArticle);
            nextArticle++;
        } else {
            nextArticle = 0;
            articleList = null;
        }
        return article;
    }

    private List<GnewsArticle> fetchArticles() {
        ResponseEntity<GnewsResponse> responseEntity = restTemplate.getForEntity(this.url, GnewsResponse.class);
        GnewsResponse response = responseEntity.getBody();
        if (response != null) {
            return response.getArticles();
        }
        return null;
    }
}
