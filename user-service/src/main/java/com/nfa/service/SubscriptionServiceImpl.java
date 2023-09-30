package com.nfa.service;

import com.nfa.dto.SubscriptionDto;
import com.nfa.entity.NewsUserEntity;
import com.nfa.entity.SubscriptionEntity;
import com.nfa.exception.NewsUserNotFoundException;
import com.nfa.repository.NewsUserRepository;
import com.nfa.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final NewsUserRepository newsUserRepository;

    @Override
    public void save(SubscriptionDto subscriptionDto, String email) throws NewsUserNotFoundException {
        NewsUserEntity newsUserEntity = newsUserRepository.findByEmail(email)
                .orElseThrow(NewsUserNotFoundException::new);

        SubscriptionEntity subscriptionEntity = toEntity(subscriptionDto);
        subscriptionRepository.save(subscriptionEntity);
        newsUserEntity.setSubscription(subscriptionEntity);
        newsUserRepository.save(newsUserEntity);
    }

    private SubscriptionEntity toEntity(SubscriptionDto dto) {
        SubscriptionEntity entity = new SubscriptionEntity();
        entity.setSource(dto.getSource());
        entity.setCategory(dto.getCategory());
        entity.setKeywords(dto.getKeywords());
        return entity;
    }
}
