package com.nfa.service;

import com.nfa.entity.Keyword;
import com.nfa.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeywordServiceImpl implements KeywordService {

    private final KeywordRepository keywordRepository;

    @Override
    public Keyword getByNameOrCreate(String name) {
        log.info("getByNameOrCreate, keyword is " + name);
        Optional<Keyword> existingKeyword = keywordRepository.findByNameIgnoreCase(name);
        log.info("optional findByNameIgnoreCase is " + existingKeyword);

        return existingKeyword.orElseGet(() -> keywordRepository.save(new Keyword(name)));
    }
}
