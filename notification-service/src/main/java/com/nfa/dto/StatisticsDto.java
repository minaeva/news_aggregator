package com.nfa.dto;

import java.time.LocalDateTime;

public record StatisticsDto(String name, String email, LocalDateTime dateCreated) {
}
