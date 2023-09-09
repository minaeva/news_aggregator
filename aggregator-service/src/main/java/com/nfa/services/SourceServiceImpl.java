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
        if (source.get() != null) {
            return entityToDto(source.get());
        }
        return null;
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

    private  List<SourceDto> entityToDtoList(List<Source> sources) {
        List<SourceDto> sourceDtoList = new ArrayList<>();
        for (Source source: sources) {
            sourceDtoList.add(new SourceDto(source.getName()));
        }
        return sourceDtoList;
    }
}
