package com.nfa.service;

import com.nfa.dto.ReaderDto;
import com.nfa.dto.SubscriptionDto;
import com.nfa.exception.ReaderNotFoundException;

public interface ReaderService {

    ReaderDto findByEmail(String email) throws ReaderNotFoundException;

    ReaderDto findByEmailAndPassword(String email, String password) throws ReaderNotFoundException;

    void save(ReaderDto readerDto);

    ReaderDto update(String email, SubscriptionDto request) throws ReaderNotFoundException;
}
