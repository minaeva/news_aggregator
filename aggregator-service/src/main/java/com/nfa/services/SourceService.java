package com.nfa.services;

import com.nfa.dto.SourceDto;
import com.nfa.entities.Source;

import java.util.List;

public interface SourceService {

    List<SourceDto> findAll();

    SourceDto findById(int theId);

    public Source getByNameOrSave(String name);

    Source save(SourceDto sourceDto);

    void deleteById(int theId);

}
