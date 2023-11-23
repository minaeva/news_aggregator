package com.nfa.service;

import com.nfa.dto.ReaderDto;
import com.nfa.dto.SubscriptionDto;
import com.nfa.dto.SubscriptionRequest;

import java.util.List;

public interface SubscriptionService {

    SubscriptionDto update(String email, SubscriptionRequest request);

    List<ReaderDto> getByKeyword(String keyword);

}
