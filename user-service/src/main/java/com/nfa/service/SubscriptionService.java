package com.nfa.service;

import com.nfa.dto.ReaderDto;
import com.nfa.dto.SubscriptionDto;
import com.nfa.dto.SubscriptionRequest;
import com.nfa.exception.KeywordNotFoundException;
import com.nfa.exception.ReaderNotFoundException;

import java.util.List;

public interface SubscriptionService {

    SubscriptionDto update(String email, SubscriptionRequest request) throws ReaderNotFoundException;

    List<ReaderDto> getByKeyword(String keyword) throws KeywordNotFoundException;

}
