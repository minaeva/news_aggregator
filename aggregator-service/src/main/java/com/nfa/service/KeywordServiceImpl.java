package com.nfa.service;

import com.nfa.entity.primary.Keyword;
import com.nfa.repository.primary.KeywordRepository;
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

    @Override
    public Keyword getByNameOrSave(String name) {
        Optional<Keyword> savedKeyword = keywordRepository.findByNameIgnoreCase(name);
        return savedKeyword.orElseGet(() -> keywordRepository.save(new Keyword(name)));
    }

    @Override
    public Optional<Keyword> getByName(String name) {
        return keywordRepository.findByNameIgnoreCase(name);
    }
}
