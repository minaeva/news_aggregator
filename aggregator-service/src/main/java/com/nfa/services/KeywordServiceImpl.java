package com.nfa.services;

import com.nfa.entities.Keyword;
import com.nfa.repositories.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KeywordServiceImpl implements KeywordService {

    private final KeywordRepository keywordRepository;

    @Override
    public List<Keyword> findAll() {
        return keywordRepository.findAll();
    }

    @Override
    public Optional<Keyword> findById(int theId) {
        return keywordRepository.findById(theId);
    }

    @Override
    public Keyword save(Keyword theKeyword) {
        return keywordRepository.save(theKeyword);
    }

    @Override
    public void deleteById(int theId) {
        keywordRepository.deleteById(theId);
    }

    public Keyword getByNameOrSave(String name) {
        Keyword savedKeyword;
        Optional<Keyword> existentCategory = keywordRepository.findByName(name);
        savedKeyword = existentCategory.orElseGet(() -> keywordRepository.save(new Keyword(name)));
        return savedKeyword;
    }
}
