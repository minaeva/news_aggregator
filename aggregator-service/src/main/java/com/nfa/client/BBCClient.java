package com.nfa.client;

import com.nfa.client.responses.BBCArticle;
import com.nfa.client.responses.BBCResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@Slf4j
public class BBCClient implements Client {

    public static final String BBC_CIRCUIT_BREAKER = "BBCCircuitBreaker";

    @Autowired
    @Qualifier("external")
    @Lazy
    private RestTemplate restTemplate;

    @Value("${data_providers.bbc.url}")
    private String url;

    @Override
    @CircuitBreaker(name = BBC_CIRCUIT_BREAKER, fallbackMethod = "fallback")
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

    private List<BBCArticle> fallback(Throwable t) {
        log.error("BBC_CIRCUIT_BREAKER is triggered");
        return null;
    }

}
