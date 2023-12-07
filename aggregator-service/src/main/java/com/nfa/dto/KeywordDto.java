package com.nfa.dto;

public record KeywordDto(String name, String rule) {

    public KeywordDto(String name) {
        this(name, null);
    }
}

