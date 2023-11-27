package com.nfa.service;

import com.nfa.repository.KeywordRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class KeywordService {

    private final KeywordRepository keywordRepository;

    public void save(String keyword) {
        log.info("saving mailing {}", keyword);
        keywordRepository.addKeyword(keyword);
    }

    public Set<String> findAll() {
        log.info("finding all keywords");
        return keywordRepository.getAllKeywords();
    }

    public void delete(String keyword) {
        log.info("deleting by keyword {}", keyword);
        keywordRepository.deleteKeyword(keyword);
    }
}
