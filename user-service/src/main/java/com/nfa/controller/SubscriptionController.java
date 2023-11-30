package com.nfa.controller;

import com.nfa.config.jwt.JwtProvider;
import com.nfa.controller.request.JwtRequest;
import com.nfa.controller.request.SubscriptionRequest;
import com.nfa.dto.ReaderDto;
import com.nfa.dto.SubscriptionDto;
import com.nfa.exception.ReaderUnauthorizedException;
import com.nfa.exception.ReaderValidationException;
import com.nfa.service.ReaderService;
import com.nfa.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.nfa.config.jwt.JwtHelper.AUTHORIZATION;
import static com.nfa.config.jwt.JwtHelper.getJwtFromString;

@RestController
@RequiredArgsConstructor
@Log
@CrossOrigin
@RequestMapping("/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final ReaderService readerService;
    private final JwtProvider jwtProvider;

    @PostMapping
    public SubscriptionDto registerSubscription(@RequestHeader(AUTHORIZATION) String jwtWithBearer,
                                                @RequestBody SubscriptionRequest request) {
        log.info("Handling registerSubscription, request: " + request);
        log.info("jwt: " + jwtWithBearer);
        String jwt = getJwtFromString(jwtWithBearer);

        if (jwt == null || !jwtProvider.validateToken(jwt)) {
            throw new ReaderUnauthorizedException("Token is invalid");
        }
        String email = jwtProvider.getEmailFromToken(jwt);
        validateSubscriptionRequest(request);
        return subscriptionService.update(email, request);
    }

    @PostMapping("/jwt")
    public SubscriptionDto getSubscriptionByJwt(@RequestBody JwtRequest jwtRequest) {
        log.info("Handling getSubscriptionByJwt, jwt: " + jwtRequest);

        String jwt = getJwtFromString(jwtRequest.getJwt());
        if (jwt == null || !jwtProvider.validateToken(jwt)) {
            throw new ReaderUnauthorizedException("Token is invalid");
        }
        String email = jwtProvider.getEmailFromToken(jwt);
        ReaderDto readerDto = readerService.findByEmail(email);
        if (readerDto != null && readerDto.getEmail() != null) {
            return subscriptionService.getByEmail(readerDto.getEmail());
        }
        return null;
    }

    @GetMapping("/{keyword}")
    public List<SubscriptionDto> getReadersByKeyword(@PathVariable("keyword") String keyword) {
        log.info("Handling getReadersByKeyword, keyword: " + keyword);
        return subscriptionService.getByKeyword(keyword);
    }

    private void validateSubscriptionRequest(SubscriptionRequest request) {
        if (request == null) {
            throw new ReaderValidationException("Request cannot be null");
        }
        if (request.getKeywordNames() == null) {
            throw new ReaderValidationException("Request keywords cannot be empty");
        }
        if (request.getTimesPerDay() == 0) {
            throw new ReaderValidationException("Request times per day cannot be zero");
        }
    }
}
