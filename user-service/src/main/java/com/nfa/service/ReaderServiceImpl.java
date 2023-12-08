package com.nfa.service;

import com.nfa.dto.ReaderDto;
import com.nfa.dto.SubscriptionDto;
import com.nfa.entity.Reader;
import com.nfa.exception.ReaderNotFoundException;
import com.nfa.exception.ReaderValidationException;
import com.nfa.repository.ReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository readerRepository;

    @Override
    public ReaderDto findByEmail(String email) {
        Reader reader = readerRepository.findByEmail(email)
                .orElseThrow(() -> new ReaderNotFoundException(
                        String.format("No reader with email %s found", email)));

        return toDto(reader);
    }

    @Override
    public ReaderDto findByEmailAndPassword(String email, String password) {
        Reader reader = readerRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new ReaderNotFoundException(
                        String.format("No reader with email %s and password found", email)));

        return toDto(reader);
    }

    @Override
    public void save(ReaderDto readerDto) {
        try {
            readerRepository.save(toEntity(readerDto));
        } catch (Exception e) {
            throw new ReaderValidationException(e.getMessage());
        }
    }

    @Override
    public ReaderDto update(String email, SubscriptionDto request) {
        Reader reader = readerRepository.findByEmail(email)
                .orElseThrow(() -> new ReaderNotFoundException(
                        String.format("No reader with email %s found", email)));
        Reader savedReader = readerRepository.save(reader);
        return toDto(savedReader);
    }

    private Reader toEntity(ReaderDto dto) {
        Reader entity = new Reader();
        entity.setName(dto.name());
        entity.setPassword(dto.password());
        entity.setEmail(dto.email());
        entity.setRole(dto.role());
        entity.setRegistrationSource(dto.registrationSource());
        return entity;
    }

    public static ReaderDto toDto(Reader reader) {
        return new ReaderDto(reader.getId(), reader.getName(), null,
                reader.getEmail(), reader.getRole(), reader.getRegistrationSource());
    }
}
