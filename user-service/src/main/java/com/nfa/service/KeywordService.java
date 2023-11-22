package com.nfa.service;

import com.nfa.dto.KeywordDto;
import com.nfa.entity.Keyword;

public interface KeywordService {

    Keyword getByNameOrCreate(String name);

    KeywordDto getDtoByNameOrCreate(String name);

}
