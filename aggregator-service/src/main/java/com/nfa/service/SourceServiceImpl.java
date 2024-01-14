package com.nfa.service;

import com.nfa.dto.SourceDto;
import com.nfa.entity.primary.Source;
import com.nfa.repository.SourceRepository;
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
        return toDtoList(sources);
    }

    @Override
    public SourceDto findById(int theId) {
        Optional<Source> source = sourceRepository.findById(theId);
        return source.map(this::toDto).orElse(null);
    }

    @Override
    public Source getByNameOrSave(String name) {
        Optional<Source> existentSource = sourceRepository.findByName(name);
        return existentSource.orElseGet(() -> sourceRepository.save(new Source(name)));
    }

    @Override
    public Source save(SourceDto sourceDto) {
        return sourceRepository.save(toEntity(sourceDto));
    }

    @Override
    public void deleteById(int theId) {
        sourceRepository.deleteById(theId);
    }

    private Source toEntity(SourceDto sourceDto) {
        return new Source(sourceDto.name());
    }

    private SourceDto toDto(Source source) {
        return new SourceDto(source.getName());
    }

    private List<SourceDto> toDtoList(List<Source> sources) {
        List<SourceDto> sourceDtoList = new ArrayList<>();
        for (Source source : sources) {
            sourceDtoList.add(new SourceDto(source.getName()));
        }
        return sourceDtoList;
    }
}
