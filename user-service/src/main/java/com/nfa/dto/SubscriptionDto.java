package com.nfa.dto;

import lombok.Data;

import java.util.List;

@Data
public class SubscriptionDto {

    private List<String> keywordNames;

    private int timesPerDay;

}
