package com.nfa.service;

import com.nfa.model.Keyword;
import com.nfa.repository.KeywordRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class KeywordService {

    private final KeywordRepository keywordRepository;

    public Keyword save(Keyword keyword) {
        log.info("saving mailing {}", keyword);
        return keywordRepository.save(keyword);
    }

    public Optional<Keyword> findByName(String name) {
        log.info("finding by keyword {}", name);
        return keywordRepository.findByName(name);
    }

    public List<Keyword> findAll() {
        log.info("finding all keywords");
        return keywordRepository.findAll();
    }

    public void delete(String name) {
        log.info("deleting by keyword {}", name);
        Optional<Keyword> existingKeyword = findByName(name);
        existingKeyword.ifPresent(keywordRepository::delete);
    }
}
