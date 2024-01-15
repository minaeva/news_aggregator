package com.nfa.repository.primary;

import com.nfa.entity.primary.Source;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SourceRepository extends JpaRepository<Source, Integer> {

    Optional<Source> findByName(String name);
}
