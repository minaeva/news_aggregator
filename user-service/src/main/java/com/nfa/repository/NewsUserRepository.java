package com.nfa.repository;

import com.nfa.entity.NewsUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NewsUserRepository extends JpaRepository<NewsUserEntity, Long> {

    Optional<NewsUserEntity> findByEmail(String email);

    Optional<NewsUserEntity> findByEmailAndPassword(String email, String password);

    List<NewsUserEntity> findBySubscription_Source(String source);
}
