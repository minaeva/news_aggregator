package com.nfa.repository;

import com.nfa.entity.Source;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SourceRepository extends JpaRepository<Source, Integer> {

    Optional<Source> findByName(String name);
}
