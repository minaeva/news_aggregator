package com.nfa.service;

import com.nfa.entity.Keyword;

public interface KeywordService {

    Keyword getByNameOrCreate(String name);

}
