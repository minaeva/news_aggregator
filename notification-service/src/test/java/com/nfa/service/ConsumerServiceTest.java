package com.nfa.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ConsumerServiceTest {

    @Mock
    private KeywordService keywordService;

    @InjectMocks
    private ConsumerService consumerService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testConsume_saveIsTriggeredOnce_whenConsumeIsCalled() {
        String testKeyword = "testKeyword";

        consumerService.consume(testKeyword);

        verify(keywordService, times(1)).save(testKeyword);
    }

}
