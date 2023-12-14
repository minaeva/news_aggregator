package com.nfa.client;

import com.nfa.dto.SubscriptionDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class ReaderClientImpl implements ReaderClient {

    public static final String USER_SERVICE_CIRCUIT_BREAKER = "UserServiceCircuitBreaker";

    @Autowired
    @Qualifier("internal")
    private RestTemplate restTemplate;

    @Value("${user.app.url}")
    private String userAppUrl;

    @Override
    @CircuitBreaker(name = USER_SERVICE_CIRCUIT_BREAKER, fallbackMethod = "fallback")
    public SubscriptionDto getSubscriptionByJwt(String jwtWithBearer) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", jwtWithBearer);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<SubscriptionDto> subscriptionDtoResponseEntity = restTemplate
                .exchange(userAppUrl, HttpMethod.GET, requestEntity, SubscriptionDto.class);
        return subscriptionDtoResponseEntity.getBody();
    }

    private SubscriptionDto fallback(String jwtWithBearer, Throwable t) {
        log.error("USER_SERVICE_CIRCUIT_BREAKER is triggered");
        return null;
    }
}
