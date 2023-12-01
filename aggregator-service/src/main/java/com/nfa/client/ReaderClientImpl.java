package com.nfa.client;

import com.nfa.dto.SubscriptionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
    public SubscriptionDto getSubscriptionByJwt(String jwt) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", jwt);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<SubscriptionDto> subscriptionDtoResponseEntity = restTemplate
                .exchange(userAppUrl, HttpMethod.GET, requestEntity, SubscriptionDto.class);
        return Objects.requireNonNull(subscriptionDtoResponseEntity.getBody());
    }
}
