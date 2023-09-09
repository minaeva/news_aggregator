package com.nfa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SourceDto {
    int id;
    String name;

    public SourceDto(int id) {
        this.id = id;
    }

    public SourceDto(String name) {
        this.name = name;
    }
}
