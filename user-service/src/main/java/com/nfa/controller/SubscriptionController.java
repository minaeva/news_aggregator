package com.nfa.controller;

import com.nfa.config.jwt.JwtProvider;
import com.nfa.dto.ReaderDto;
import com.nfa.dto.SubscriptionDto;
import com.nfa.exception.ReaderNotFoundException;
import com.nfa.exception.ReaderValidationException;
import com.nfa.service.ReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;

import static com.nfa.config.jwt.JwtHelper.AUTHORIZATION;
import static com.nfa.config.jwt.JwtHelper.getJwtFromString;

@RestController
@RequiredArgsConstructor
@Log
@CrossOrigin
@RequestMapping("/subscription")
public class SubscriptionController {

    private final ReaderService readerService;
    private final JwtProvider jwtProvider;

    @PostMapping
    public ReaderDto registerSubscription(@RequestHeader(AUTHORIZATION) String jwtWithBearer, @RequestBody SubscriptionDto request)
            throws ReaderValidationException, ReaderNotFoundException {
        log.info("handling register subscription request: " + request);
        log.info("jwt: " + jwtWithBearer);
        String jwt = getJwtFromString(jwtWithBearer);

        if (jwt != null && jwtProvider.validateToken(jwt)) {
            String email = jwtProvider.getEmailFromToken(jwt);
            validateSubscriptionRequest(request);
            return readerService.update(email, request);
        }
        return null;
    }

    private void validateSubscriptionRequest(SubscriptionDto request) throws ReaderValidationException {
        if (request == null || request.getKeywordNames() == null || request.getTimesPerDay() == 0)  {
            throw new ReaderValidationException("request cannot be null");
        }
    }
}
