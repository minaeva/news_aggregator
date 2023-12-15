package com.nfa.client;

import com.nfa.dto.SubscriptionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${user.service.eureka.name}", fallback = UserClientFallback.class)
public interface UserClient {

    @GetMapping("${subscription.by.jwt.path}")
    SubscriptionDto getSubscriptionByJwt(@RequestHeader("Authorization") String jwt);

}
