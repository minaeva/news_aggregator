package com.nfa.service;

import com.nfa.dto.ReaderDto;
import com.nfa.dto.SubscriptionDto;

public interface ReaderService {

    ReaderDto findByEmail(String email);

    ReaderDto findByEmailAndPassword(String email, String password);

    void save(ReaderDto readerDto);

    ReaderDto update(String email, SubscriptionDto request);
}
