package com.nfa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeywordDto {
    int id;
    String name;
    String rule;

    public KeywordDto(int id) {
        this.id = id;
    }

    public KeywordDto(String name) {
        this.name = name;
    }

}

