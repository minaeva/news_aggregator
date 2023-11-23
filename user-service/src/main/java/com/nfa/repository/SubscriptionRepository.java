package com.nfa.repository;

import com.nfa.entity.Keyword;
import com.nfa.entity.Reader;
import com.nfa.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

    Optional<Subscription> findByReader(Reader reader);

    Optional<List<Subscription>> findByKeywordsContaining(Keyword keyword);
}
