package com.nfa.batch.readers;

import com.nfa.client.Client;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;

import java.util.Iterator;
import java.util.List;

@Slf4j
public class RestApiItemReader<T> implements ItemReader {

    private final Client client;
    private final Class<T> targetType;
    private Iterator<T> dataIterator;
    private List<T> cachedData;

    public RestApiItemReader(Client client, Class<T> targetType) {
        this.client = client;
        this.targetType = targetType;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @Override
    @CircuitBreaker(name = "myCircuitBreaker", fallbackMethod = "fallback")
    public T read() {
        if (fetchDataFromApi() == null) {
            return null;
        }
        if (dataIterator == null) {
            dataIterator = fetchDataFromApi().listIterator();
        }
        return dataIterator.hasNext() ? dataIterator.next() : null;
    }

    private List<T> fetchDataFromApi() {
        if (cachedData == null) {
            cachedData = client.fetchData();
            log.info("From {} have fetched news {}", client, cachedData);
        }
        return cachedData;
    }

    private ResponseEntity<String> fallback(Throwable t) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Fallback Response");
    }

}
