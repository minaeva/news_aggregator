package com.nfa.service;

import com.nfa.dto.NewsUserDto;
import com.nfa.exception.NewsUserNotFoundException;

import java.util.Set;

public interface NewsUserService {

    NewsUserDto findByEmail(String email) throws NewsUserNotFoundException;

    NewsUserDto findByEmailAndPassword(String email, String password) throws NewsUserNotFoundException;

    void save(NewsUserDto newsUserDto);

    Set<Long> findAllBySource(String source);
}
