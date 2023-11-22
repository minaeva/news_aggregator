package com.nfa.dto;

import lombok.Data;

import java.util.List;

@Data
public class SubscriptionDto {

    private Long readerId;

    private List<String> keywordNames;

    private Integer timesPerDay;

}
