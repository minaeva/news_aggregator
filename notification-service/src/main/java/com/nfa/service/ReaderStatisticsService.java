package com.nfa.service;

import com.nfa.dto.StatisticsDto;
import com.nfa.dto.SubscriptionDto;

public interface ReaderStatisticsService {

    StatisticsDto save(SubscriptionDto subscriptionDto);
}
