package com.nfa.service;

import com.nfa.entity.Keyword;
import com.nfa.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KeywordServiceImpl implements KeywordService {

    private final KeywordRepository keywordRepository;

    @Override
    public Keyword getByNameOrCreate(String name) {
        Optional<Keyword> existingKeyword = keywordRepository.findByNameIgnoreCase(name);
        return existingKeyword.orElseGet(() -> keywordRepository.save(new Keyword(name)));
    }
}
