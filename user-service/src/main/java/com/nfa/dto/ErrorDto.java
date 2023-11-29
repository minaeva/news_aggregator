package com.nfa.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorDto {

    private String message;
    private LocalDateTime time;
}
