package com.nfa.repository.primary;

import com.nfa.entity.primary.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Integer> {

    Optional<Keyword> findByNameIgnoreCase(String name);
}
