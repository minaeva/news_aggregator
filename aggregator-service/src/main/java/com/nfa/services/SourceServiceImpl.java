package com.nfa.services;

import com.nfa.dto.SourceDto;
import com.nfa.entities.Source;
import com.nfa.repositories.SourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SourceServiceImpl implements SourceService {
    private final SourceRepository sourceRepository;

    @Override
    public List<SourceDto> findAll() {
        List<Source> sources = sourceRepository.findAll();
        return entityToDtoList(sources);
    }

    @Override
    public SourceDto findById(int theId) {
        Optional<Source> source = sourceRepository.findById(theId);
        return source.map(this::entityToDto).orElse(null);
    }

    @Override
    public Source getByNameOrSave(String name) {
        Optional<Source> existentSource = sourceRepository.findByName(name);
        Source savedSource = existentSource.orElseGet(() -> sourceRepository.save(new Source(name)));
        return savedSource;
    }

    @Override
    public Source save(SourceDto sourceDto) {
        return sourceRepository.save(dtoToEntity(sourceDto));
    }

    @Override
    public void deleteById(int theId) {
        sourceRepository.deleteById(theId);
    }

    private Source dtoToEntity(SourceDto sourceDto) {
        return new Source(sourceDto.getName());
    }

    private SourceDto entityToDto(Source source) {
        return new SourceDto(source.getName());
    }

    private List<SourceDto> entityToDtoList(List<Source> sources) {
        List<SourceDto> sourceDtoList = new ArrayList<>();
        for (Source source : sources) {
            sourceDtoList.add(new SourceDto(source.getName()));
        }
        return sourceDtoList;
    }
}
