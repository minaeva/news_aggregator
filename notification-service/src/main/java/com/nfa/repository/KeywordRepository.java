package com.nfa.repository;

import com.nfa.model.Keyword;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KeywordRepository extends CrudRepository<Keyword, Integer> {

    Optional<Keyword> findByName(String name);

    List<Keyword> findAll();
}
