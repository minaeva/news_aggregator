package com.nfa.controller;

import com.nfa.config.jwt.JwtProvider;
import com.nfa.dto.SubscriptionDto;
import com.nfa.exception.NewsUserNotFoundException;
import com.nfa.exception.NewsUserValidationException;
import com.nfa.service.NewsUserService;
import com.nfa.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static com.nfa.config.jwt.JwtHelper.AUTHORIZATION;
import static com.nfa.config.jwt.JwtHelper.getJwtFromString;

@RestController
@RequiredArgsConstructor
@Log
@CrossOrigin
@RequestMapping("/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final NewsUserService newsUserService;
    private final JwtProvider jwtProvider;

    @PostMapping
    public void registerSubscription(@RequestHeader(AUTHORIZATION) String jwtWithBearer, @RequestBody SubscriptionDto request)
            throws NewsUserValidationException, NewsUserNotFoundException {
        log.info("handling register subscription request: " + request);
        log.info("jwt: " + jwtWithBearer);
        String jwt = getJwtFromString(jwtWithBearer);

        if (jwt != null && jwtProvider.validateToken(jwt)) {
            String email = jwtProvider.getEmailFromToken(jwt);
            validateSubscriptionRequest(request);
            subscriptionService.save(request, email);
        }
    }

    @GetMapping("/{source}/users")
    public Set<Long> getUserIdsBySource(@PathVariable String source) {
        return newsUserService.findAllBySource(source);
    }

    private void validateSubscriptionRequest(SubscriptionDto request) throws NewsUserValidationException {
        if (request == null) {
            throw new NewsUserValidationException("request cannot be null");
        }
        if (request.getCategory() == null && request.getKeywords() == null && request.getSource() == null) {
            throw new NewsUserValidationException("request cannot be null");
        }
    }
}
