package com.nfa.client;

import com.nfa.controller.JwtRequest;
import com.nfa.dto.SubscriptionDto;

public interface ReaderClient {

    SubscriptionDto getSubscriptionByJwt(String jwt);
}
