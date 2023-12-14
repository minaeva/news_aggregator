package com.nfa.client;

import com.nfa.client.responses.NYTArticle;
import com.nfa.client.responses.NYTResponse;
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
public class NYTClient implements Client {

    public static final String NYT_CIRCUIT_BREAKER = "NYTCircuitBreaker";

    @Autowired
    @Qualifier("external")
    @Lazy
    private RestTemplate restTemplate;

    @Value("${data_providers.nyt.url}")
    private String url;

    @Override
    @CircuitBreaker(name = NYT_CIRCUIT_BREAKER, fallbackMethod = "fallback")
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

    private List<NYTArticle> fallback(Throwable t) {
        log.error("NYT_CIRCUIT_BREAKER is triggered");
        return null;
    }
}
