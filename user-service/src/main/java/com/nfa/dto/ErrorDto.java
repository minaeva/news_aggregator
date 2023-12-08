package com.nfa.dto;

import java.time.LocalDateTime;

public record ErrorDto(String message, LocalDateTime time) {
}
