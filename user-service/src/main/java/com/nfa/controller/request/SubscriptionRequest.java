package com.nfa.controller.request;

import lombok.Data;

import java.util.List;

@Data
public class SubscriptionRequest {

    private List<String> keywordNames;

    private int timesPerDay;

}
