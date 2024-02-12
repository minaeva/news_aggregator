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
        log.info("findByNameIgnoreCase is ", existingKeyword.get());
        Optional<Keyword> test = keywordRepository.findByName(name);
        log.info("optional findByName is " + test);
        log.info("findByName is ", test.get());

        return existingKeyword.orElseGet(() -> keywordRepository.save(new Keyword(name)));
    }
}
