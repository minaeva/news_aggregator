package com.nfa.services;

import com.nfa.entities.Keyword;

import java.util.List;
import java.util.Optional;

public interface KeywordService {

    List<Keyword> findAll();

    Optional<Keyword> findById(int theId);

    Keyword save(Keyword theKeyword);

    void deleteById(int theId);

    public Keyword getByNameOrSave(String name);
}
