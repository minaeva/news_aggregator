package com.nfa.service;

import com.nfa.dto.SubscriptionDto;
import com.nfa.controller.request.SubscriptionRequest;
import com.nfa.entity.Keyword;
import com.nfa.entity.Reader;
import com.nfa.entity.Subscription;
import com.nfa.exception.ReaderNotFoundException;
import com.nfa.repository.KeywordRepository;
import com.nfa.repository.ReaderRepository;
import com.nfa.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final ReaderRepository readerRepository;
    private final KeywordService keywordService;
    private final SubscriptionRepository subscriptionRepository;
    private final KeywordRepository keywordRepository;

    @Override
    public SubscriptionDto update(String email, SubscriptionRequest request) {
        Reader reader = readerRepository.findByEmail(email)
                .orElseThrow(() -> new ReaderNotFoundException("No reader with email " + email));

        Subscription subscription = reader.getSubscription();
        if (subscription == null) {
            subscription = new Subscription();
        }

        subscription.setKeywords(getKeywords(request));
        subscription.setTimesPerDay(request.getTimesPerDay());
        subscription.setReader(reader);
        Subscription updatedSubscription = subscriptionRepository.save(subscription);

        reader.setSubscription(updatedSubscription);
        readerRepository.save(reader);

        return toDto(updatedSubscription);
    }

    @Override
    public List<SubscriptionDto> getByKeyword(String keyword) {
        Optional<Keyword> existingKeyword = keywordRepository.findByNameIgnoreCase(keyword);
        if (existingKeyword.isEmpty()) {
            return List.of();
        }

        Optional<List<Subscription>> subscriptions = subscriptionRepository.findByKeywordsContaining(existingKeyword.get());

        return subscriptions.map(subscriptionList -> subscriptionList.stream()
                .map(this::toDto)
                .collect(toList())).orElse(List.of());
    }

    @Override
    public SubscriptionDto getByEmail(String email) {

        Optional<Subscription> subscription = subscriptionRepository.findByReaderEmail(email);
        return subscription.map(this::toDto).orElse(null);
    }

    private Set<Keyword> getKeywords(SubscriptionRequest request) {
        Set<Keyword> keywordList = new HashSet<>();
        List<String> keywordNames = request.getKeywordNames();
        for (String keywordName : keywordNames) {
            Keyword savedKeyword = keywordService.getByNameOrCreate(keywordName);
            keywordList.add(savedKeyword);
        }
        return keywordList;
    }

    private SubscriptionDto toDto(Subscription subscription) {
        List<String> keywordNames = subscription.getKeywords().stream().map(Keyword::getName).collect(toList());

        return new SubscriptionDto(
                subscription.getReader().getId(),
                subscription.getReader().getName(),
                subscription.getReader().getEmail(),
                keywordNames,
                subscription.getTimesPerDay());
    }
}
