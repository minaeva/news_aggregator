package com.nfa.service;

import com.nfa.dto.NewsUserDto;
import com.nfa.exception.NewsUserNotFoundException;

public interface NewsUserService {

    NewsUserDto findByEmail(String email) throws NewsUserNotFoundException;

    NewsUserDto findByEmailAndPassword(String email, String password) throws NewsUserNotFoundException;

    void save(NewsUserDto newsUserDto);
}
