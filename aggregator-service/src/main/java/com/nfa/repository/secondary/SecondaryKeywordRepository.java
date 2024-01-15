package com.nfa.repository.secondary;

import com.nfa.entity.secondary.SecondaryKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecondaryKeywordRepository extends JpaRepository<SecondaryKeyword, Integer> {
}
