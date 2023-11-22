package com.nfa.dto;

import lombok.Data;

@Data
public class KeywordDto {

    private Long id;

    private String name;

    public KeywordDto(String name) {
        this.name = name;
    }
}
