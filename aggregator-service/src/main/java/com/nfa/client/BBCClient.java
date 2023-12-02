package com.nfa.client;

import com.nfa.client.responses.BBCArticle;
import com.nfa.client.responses.BBCResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@Slf4j
public class BBCClient implements Client {

    @Autowired
    @Lazy
    private RestTemplate restTemplate;

    @Value("${data_providers.bbc.url}")
    private String url;

    @Override
    public List<BBCArticle> fetchData() {
        try {
            BBCResponse response = restTemplate.getForObject(this.url, BBCResponse.class);
            if (response != null) {
                return response.getArticles();
            }
        } catch (Exception ex) {
            log.info("error " + ex.getMessage());
        }
        return null;
    }
}
