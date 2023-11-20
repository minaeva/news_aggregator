package com.nfa.client;

import com.nfa.client.responses.GnewsArticle;
import com.nfa.client.responses.GnewsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@Slf4j
public class GnewsClient implements Client {

    @Autowired
    @Lazy
    private RestTemplate restTemplate;

    @Value("${data_providers.gnews.url}")
    private String url;

    @Override
    public List<GnewsArticle> fetchData() {
        try {
            GnewsResponse response = restTemplate.getForObject(this.url, GnewsResponse.class);
            if (response != null) {
                return response.getArticles();
            }
        } catch (HttpClientErrorException ex) {
            log.info("error " + ex.getStatusCode());
        }
        return null;
    }
}
