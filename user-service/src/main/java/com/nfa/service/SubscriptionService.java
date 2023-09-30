package com.nfa.service;

import com.nfa.dto.SubscriptionDto;
import com.nfa.exception.NewsUserNotFoundException;

public interface SubscriptionService {

    void save(SubscriptionDto subscriptionDto, String email) throws NewsUserNotFoundException;
}
