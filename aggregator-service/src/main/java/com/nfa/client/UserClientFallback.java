package com.nfa.client;

import com.nfa.dto.SubscriptionDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserClientFallback implements UserClient {

    @Override
    public SubscriptionDto getSubscriptionByJwt(String jwt) {
        log.error("User client Fallback is triggered");
        return null;
    }
}
