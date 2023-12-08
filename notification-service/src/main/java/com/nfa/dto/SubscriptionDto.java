package com.nfa.dto;

import java.util.List;

public record SubscriptionDto(Long readerId, String readerName, String readerEmail,
                              List<String> keywordNames, Integer timesPerDay) {
}