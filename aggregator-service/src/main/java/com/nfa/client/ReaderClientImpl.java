package com.nfa.client;

import com.nfa.controller.JwtRequest;
import com.nfa.dto.SubscriptionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
@Slf4j
public class ReaderClientImpl implements ReaderClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${user.app.url}")
    private String userAppUrl;


    @Override
    public SubscriptionDto getSubscriptionByJwt(JwtRequest jwtRequest) {
        log.info("getSubscriptionsByJwt {}", jwtRequest);
        ResponseEntity<SubscriptionDto> subscriptionDtoResponseEntity = restTemplate.postForEntity(userAppUrl, jwtRequest, SubscriptionDto.class);
        log.info("keywords {}", subscriptionDtoResponseEntity.getBody().getKeywordNames());
        return Objects.requireNonNull(subscriptionDtoResponseEntity.getBody());
    }
}
