package com.nfa.service;

import com.nfa.dto.ReaderDto;

public interface ReaderService {

    ReaderDto findByEmail(String email);

    ReaderDto authenticate(String email, String password);

    void save(ReaderDto readerDto);
}
