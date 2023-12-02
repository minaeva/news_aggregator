package com.nfa.client;

import com.nfa.client.responses.NYTArticle;
import com.nfa.client.responses.NYTResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@Slf4j
public class NYTClient implements Client {

    @Autowired
    @Lazy
    private RestTemplate restTemplate;

    @Value("${data_providers.nyt.url}")
    private String url;

    @Override
    public List<NYTArticle> fetchData() {
        try {
            NYTResponse response = restTemplate.getForObject(this.url, NYTResponse.class);
            if (response != null) {
                return response.getResults();
            }
        } catch (Exception ex) {
            log.info("error " + ex.getMessage());
        }
        return null;
    }
}
