package com.nfa.controller;

import com.nfa.config.CustomNewsUserDetails;
import com.nfa.config.jwt.JwtProvider;
import com.nfa.controller.request.SubscriptionRequest;
import com.nfa.dto.ReaderDto;
import com.nfa.dto.SubscriptionDto;
import com.nfa.exception.ReaderUnauthorizedException;
import com.nfa.exception.RequestValidationException;
import com.nfa.service.ReaderService;
import com.nfa.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
                                                @RequestBody @Valid SubscriptionRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> messages = new ArrayList<>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                messages.add(error.getDefaultMessage());
            }
            String message = StringUtils.join(messages, " : ");
            throw new RequestValidationException(message);
        }
        log.info("Handling registerSubscription, request: " + request);
        log.info("jwt: " + jwtWithBearer);
        String jwt = getJwtFromString(jwtWithBearer);

        if (jwt == null || !jwtProvider.validateToken(jwt)) {
            throw new ReaderUnauthorizedException("Token is invalid");
        }
        String email = jwtProvider.getEmailFromToken(jwt);
        return subscriptionService.update(email, request);
    }

    @GetMapping("/jwt")
    public SubscriptionDto getSubscriptionByJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomNewsUserDetails principal = (CustomNewsUserDetails) authentication.getPrincipal();
        ReaderDto readerDto = readerService.findByEmail(principal.getUsername());
        if (readerDto != null && readerDto.email() != null) {
            return subscriptionService.getByEmail(readerDto.email());
        }
        return null;
    }

    @GetMapping("/{keyword}")
    public List<SubscriptionDto> getReadersByKeyword(@PathVariable("keyword") String keyword) {
        log.info("Handling getReadersByKeyword, keyword: " + keyword);
        return subscriptionService.getByKeyword(keyword);
    }

}
