package com.nfa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    int id;
    String name;

    public CategoryDto(int id) {
        this.id = id;
    }

    public CategoryDto(String name) {
        this.name = name;
    }

}

