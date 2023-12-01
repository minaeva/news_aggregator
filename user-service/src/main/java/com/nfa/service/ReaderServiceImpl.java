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
                .orElseThrow(() -> new ReaderNotFoundException("No reader with email " + email));

        return toDto(reader);
    }

    @Override
    public ReaderDto findByEmailAndPassword(String email, String password) {
        Reader reader = readerRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new ReaderNotFoundException("No reader with email " + email));

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
                .orElseThrow(() -> new ReaderNotFoundException("No reader with email " + email));
        Reader savedReader = readerRepository.save(reader);
        return toDto(savedReader);
    }

    private Reader toEntity(ReaderDto dto) {
        Reader entity = new Reader();
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setRole(dto.getRole());
        entity.setRegistrationSource(dto.getRegistrationSource());
        return entity;
    }

    public static ReaderDto toDto(Reader reader) {
        ReaderDto readerDto = new ReaderDto();
        readerDto.setId(reader.getId());
        readerDto.setName(reader.getName());
        readerDto.setEmail(reader.getEmail());
        readerDto.setRole(reader.getRole());
        readerDto.setRegistrationSource(reader.getRegistrationSource());
        return readerDto;
    }
}
