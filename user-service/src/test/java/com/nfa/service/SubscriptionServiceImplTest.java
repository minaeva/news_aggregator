package com.nfa.service;

import com.nfa.controller.request.SubscriptionRequest;
import com.nfa.exception.ReaderNotFoundException;
import com.nfa.repository.KeywordRepository;
import com.nfa.repository.ReaderRepository;
import com.nfa.repository.SubscriptionRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
@FieldDefaults(level = AccessLevel.PRIVATE)
class SubscriptionServiceImplTest {

    @Mock
    ReaderRepository readerRepository;
    @Mock
    KeywordService keywordService;
    @Mock
    SubscriptionRepository subscriptionRepository;
    @Mock
    KeywordRepository keywordRepository;

    @InjectMocks
    SubscriptionServiceImpl subject;

    @BeforeEach
    public void setup() {
        subject = new SubscriptionServiceImpl(readerRepository, subscriptionRepository, keywordRepository, keywordService);
    }

    @Test
    void update_whenReaderNotFound_shouldThrowReaderNotFoundException() {
        String readerEmail = "reader@gmail.com";
        SubscriptionRequest request = new SubscriptionRequest();
        when(readerRepository.findByEmail(readerEmail))
                .thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> subject.update(readerEmail, request));
        Assertions.assertThat(throwable).isInstanceOf(ReaderNotFoundException.class);
    }

    @Test
    void getByKeyword() {
    }

    @Test
    void getByEmail() {
    }
}