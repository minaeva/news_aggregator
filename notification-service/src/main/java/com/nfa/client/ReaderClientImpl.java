package com.nfa.client;

import com.nfa.dto.SubscriptionDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Component
@AllArgsConstructor
@Slf4j
public class ReaderClientImpl implements ReaderClient {

    private final RestTemplate restTemplate;

    @Override
    public List<SubscriptionDto> getSubscriptionsByKeyword(String keyword) {

        log.info("getSubscriptionsByKeyword {}", keyword);
        String url = String.format("http://user-app/subscription/%s", keyword);
        ResponseEntity<SubscriptionDto[]> responseEntity = restTemplate
                .getForEntity(url, SubscriptionDto[].class);

        HttpStatusCode statusCode = responseEntity.getStatusCode();
        log.info("status code {}", statusCode);

        log.info("body {}", responseEntity.getBody());
        return List.of(Objects.requireNonNull(responseEntity.getBody()));
    }
}
