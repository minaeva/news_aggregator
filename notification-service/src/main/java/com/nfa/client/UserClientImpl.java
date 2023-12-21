package com.nfa.client;

import com.nfa.dto.SubscriptionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class UserClientImpl implements UserClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${user.app.url}")
    private String userAppUrl;

    @Override
    public List<SubscriptionDto> getSubscriptionsByKeyword(String keyword) {

        log.info("getSubscriptionsByKeyword {}", keyword);
        String url = String.format(userAppUrl, keyword);
        ResponseEntity<SubscriptionDto[]> responseEntity = restTemplate
                .getForEntity(url, SubscriptionDto[].class);

        HttpStatusCode statusCode = responseEntity.getStatusCode();
        log.info("status code {}", statusCode);

        log.info("body {}", responseEntity.getBody());
        return List.of(Objects.requireNonNull(responseEntity.getBody()));
    }
}
