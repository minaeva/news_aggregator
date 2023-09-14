package com.nfa.client;

import com.nfa.client.responses.GnewsArticle;
import com.nfa.client.responses.GnewsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class GnewsClient implements Client {

    @Autowired
    @Lazy
    private RestTemplate restTemplate;

    @Value("${data_providers.gnews.url}")
    private String url;

    @Override
    public List<GnewsArticle> fetchData() {
        ResponseEntity<GnewsResponse> responseEntity = restTemplate.getForEntity(this.url, GnewsResponse.class);
        GnewsResponse response = responseEntity.getBody();
        if (response != null) {
            return response.getArticles();
        }
        return null;
    }
}
