package com.nfa.client;

import com.nfa.client.responses.BBCArticle;
import com.nfa.client.responses.BBCResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class BBCClient implements Client {

    @Autowired
    @Lazy
    private RestTemplate restTemplate;

    @Value("${data_providers.bbc.url}")
    private String url;

    @Override
    public List<BBCArticle> fetchData() {
        ResponseEntity<BBCResponse> responseEntity = restTemplate.getForEntity(this.url, BBCResponse.class);
        BBCResponse response = responseEntity.getBody();
        if (response != null) {
            return response.getArticles();
        }
        return null;
    }
}
