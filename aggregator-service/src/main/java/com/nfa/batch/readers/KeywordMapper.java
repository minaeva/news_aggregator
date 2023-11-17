package com.nfa.batch.readers;

import com.nfa.dto.KeywordDto;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class KeywordMapper implements FieldSetMapper<KeywordDto> {

    @Override
    public KeywordDto mapFieldSet(FieldSet fieldSet) {
        return KeywordDto.builder()
                .name(fieldSet.readString("name"))
                .rule(fieldSet.readString("rule"))
                .build();
    }
}