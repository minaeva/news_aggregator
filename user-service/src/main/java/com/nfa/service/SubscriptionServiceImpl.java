package com.nfa.service;

import com.nfa.dto.ReaderDto;
import com.nfa.dto.SubscriptionDto;
import com.nfa.dto.SubscriptionRequest;
import com.nfa.entity.Keyword;
import com.nfa.entity.Reader;
import com.nfa.entity.Subscription;
import com.nfa.exception.KeywordNotFoundException;
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
    public SubscriptionDto update(String email, SubscriptionRequest request) throws ReaderNotFoundException {
        Reader reader = readerRepository.findByEmail(email)
                .orElseThrow(ReaderNotFoundException::new);

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
    public List<ReaderDto> getByKeyword(String keyword) throws KeywordNotFoundException {
        Keyword existingKeyword = keywordRepository.findByName(keyword)
                .orElseThrow(KeywordNotFoundException::new);

        Optional<List<Subscription>> subscriptions = subscriptionRepository.findByKeywordsContaining(existingKeyword);

        return subscriptions.map(subscriptionList -> subscriptionList.stream()
                .map(Subscription::getReader)
                .map(ReaderServiceImpl::toDto)
                .collect(toList())).orElse(null);
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

    private static SubscriptionDto toDto(Subscription subscription) {
        SubscriptionDto result = new SubscriptionDto();
        result.setReaderId(subscription.getReader().getId());
        result.setTimesPerDay(subscription.getTimesPerDay());
        List<String> keywordNames = subscription.getKeywords().stream().map(Keyword::getName).collect(toList());
        result.setKeywordNames(keywordNames);
        return result;
    }
}
