package com.nfa.service;

import com.nfa.dto.ReaderDto;
import com.nfa.dto.SubscriptionDto;
import com.nfa.entity.Reader;
import com.nfa.exception.ReaderNotFoundException;
import com.nfa.repository.ReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository readerRepository;
    private final KeywordService keywordService;

    @Override
    public ReaderDto findByEmail(String email) throws ReaderNotFoundException {
        Reader reader = readerRepository.findByEmail(email)
                .orElseThrow(ReaderNotFoundException::new);

        return toDto(reader);
    }

    @Override
    public ReaderDto findByEmailAndPassword(String email, String password) throws ReaderNotFoundException {
        Reader reader = readerRepository.findByEmailAndPassword(email, password)
                .orElseThrow(ReaderNotFoundException::new);

        return toDto(reader);
    }

    @Override
    public void save(ReaderDto readerDto) {
        readerRepository.save(toEntity(readerDto));
    }

    //TODO: check if needed
    @Override
    public ReaderDto update(String email, SubscriptionDto request) throws ReaderNotFoundException {
        Reader reader = readerRepository.findByEmail(email)
                .orElseThrow(ReaderNotFoundException::new);
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
