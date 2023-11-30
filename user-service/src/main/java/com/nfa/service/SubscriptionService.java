package com.nfa.service;

import com.nfa.controller.request.SubscriptionRequest;
import com.nfa.dto.SubscriptionDto;

import java.util.List;

public interface SubscriptionService {

    SubscriptionDto update(String email, SubscriptionRequest request);

    List<SubscriptionDto> getByKeyword(String keyword);

    SubscriptionDto getByEmail(String email);
}
