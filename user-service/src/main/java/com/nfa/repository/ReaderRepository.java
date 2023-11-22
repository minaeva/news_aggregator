package com.nfa.repository;

import com.nfa.entity.Reader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReaderRepository extends JpaRepository<Reader, Long> {

    Optional<Reader> findByEmail(String email);

    Optional<Reader> findByEmailAndPassword(String email, String password);

}
