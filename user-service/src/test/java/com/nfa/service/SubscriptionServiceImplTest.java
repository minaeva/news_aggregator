package com.nfa.service;

import com.nfa.controller.request.SubscriptionRequest;
import com.nfa.dto.SubscriptionDto;
import com.nfa.entity.Keyword;
import com.nfa.entity.Reader;
import com.nfa.entity.Subscription;
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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

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
    void update_whenNoSubscription_shouldCreateSubscription() {
        String readerEmail = "reader@gmail.com";

        Reader readerWithNoSubscription = new Reader();
        readerWithNoSubscription.setEmail(readerEmail);

        SubscriptionRequest request = new SubscriptionRequest();
        request.setTimesPerDay(1);
        request.setKeywordNames(List.of("first"));
        Keyword keywordFirst = new Keyword("first");

        when(readerRepository.findByEmail(readerEmail))
                .thenReturn(Optional.of(readerWithNoSubscription));
        when(keywordService.getByNameOrCreate("first"))
                .thenReturn(keywordFirst);
        when(readerRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArguments()[0]);
        when(subscriptionRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArguments()[0]);

        SubscriptionDto result = subject.update(readerEmail, request);

        ArgumentCaptor<Subscription> subscriptionArgumentCaptor = ArgumentCaptor.forClass(Subscription.class);
        verify(subscriptionRepository, times(1)).save(subscriptionArgumentCaptor.capture());
        assertThat(subscriptionArgumentCaptor.getValue().getReader()).isEqualTo(readerWithNoSubscription);
        ArgumentCaptor<Reader> readerArgumentCaptor = ArgumentCaptor.forClass(Reader.class);
        verify(readerRepository, times(1)).save(readerArgumentCaptor.capture());
        assertThat(readerArgumentCaptor.getValue().getEmail()).isEqualTo(readerEmail);

        assertThat(result.readerEmail()).isEqualTo(readerEmail);
        assertThat(result.keywordNames()).isEqualTo(request.getKeywordNames());
        assertThat(result.timesPerDay()).isEqualTo(request.getTimesPerDay());
    }

    @Test
    void whenSubscriptionExists_shouldUpdateKeywordsAndTimesPerDay() {
        String readerEmail = "reader@gmail.com";

        Subscription existingSubscription = new Subscription();
        existingSubscription.setTimesPerDay(1);
        existingSubscription.setKeywords(Set.of(new Keyword("one")));
        Reader readerWithSubscription = new Reader();
        readerWithSubscription.setEmail(readerEmail);
        readerWithSubscription.setSubscription(existingSubscription);

        SubscriptionRequest request = new SubscriptionRequest();
        request.setTimesPerDay(2);
        request.setKeywordNames(List.of("two"));
        Keyword keywordTwo = new Keyword("two");

        when(readerRepository.findByEmail(readerEmail))
                .thenReturn(Optional.of(readerWithSubscription));
        when(keywordService.getByNameOrCreate("two"))
                .thenReturn(keywordTwo);
        when(readerRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArguments()[0]);
        when(subscriptionRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArguments()[0]);

        SubscriptionDto result = subject.update(readerEmail, request);

        ArgumentCaptor<Subscription> subscriptionArgumentCaptor = ArgumentCaptor.forClass(Subscription.class);
        verify(subscriptionRepository, times(1)).save(subscriptionArgumentCaptor.capture());
        assertThat(subscriptionArgumentCaptor.getValue().getReader()).isEqualTo(readerWithSubscription);
        ArgumentCaptor<Reader> readerArgumentCaptor = ArgumentCaptor.forClass(Reader.class);
        verify(readerRepository, times(1)).save(readerArgumentCaptor.capture());
        assertThat(readerArgumentCaptor.getValue().getEmail()).isEqualTo(readerEmail);

        assertThat(result.readerEmail()).isEqualTo(readerEmail);
        assertThat(result.keywordNames()).isEqualTo(request.getKeywordNames());
        assertThat(result.timesPerDay()).isEqualTo(request.getTimesPerDay());
    }

    @Test
    void getByKeyword() {
    }

    @Test
    void getByEmail() {
    }
}