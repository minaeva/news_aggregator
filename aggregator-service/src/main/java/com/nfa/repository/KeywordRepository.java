package com.nfa.repository;

import com.nfa.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Integer> {

    Optional<Keyword> findByName(String name);
}
