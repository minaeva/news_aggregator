package com.nfa.client;

import com.nfa.dto.SubscriptionDto;

import java.util.List;

public interface ReaderClient {

    List<SubscriptionDto> getSubscriptionsByKeyword(String keyword);

}
