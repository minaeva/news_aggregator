package com.nfa.client;

import com.nfa.client.responses.GnewsArticle;
import com.nfa.client.responses.GnewsResponse;
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
public class GnewsClient implements Client {

    public static final String GNEWS_CIRCUIT_BREAKER = "GnewsCircuitBreaker";

    @Autowired
    @Qualifier("external")
    @Lazy
    private RestTemplate restTemplate;

    @Value("${data_providers.gnews.url}")
    private String url;

    @Override
    @CircuitBreaker(name = GNEWS_CIRCUIT_BREAKER, fallbackMethod = "fallback")
    public List<GnewsArticle> fetchData() {
        try {
            GnewsResponse response = restTemplate.getForObject(this.url, GnewsResponse.class);
            if (response != null) {
                return response.getArticles();
            }
        } catch (Exception ex) {
            log.info("error " + ex.getMessage());
        }
        return null;
    }

    private List<GnewsArticle> fallback(Throwable t) {
        log.error("GNEWS_CIRCUIT_BREAKER is triggered");
        return null;
    }
}
