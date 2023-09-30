package com.nfa.controller;

import com.nfa.config.jwt.JwtProvider;
import com.nfa.controller.request.AuthRequest;
import com.nfa.controller.request.RegistrationRequest;
import com.nfa.controller.response.AuthResponse;
import com.nfa.controller.response.RegistrationResponse;
import com.nfa.dto.NewsUserDto;
import com.nfa.entity.RegistrationSource;
import com.nfa.exception.NewsUserNotFoundException;
import com.nfa.exception.NewsUserUnauthorizedException;
import com.nfa.exception.NewsUserValidationException;
import com.nfa.service.NewsUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;

import static com.nfa.config.jwt.JwtHelper.AUTHORIZATION;
import static com.nfa.config.jwt.JwtHelper.getJwtFromString;
import static com.nfa.controller.RegistrationRequestValidator.validateRegistrationRequest;

@RestController
@RequiredArgsConstructor
@Log
@CrossOrigin
public class AuthController {

    private final NewsUserService newsUserService;
    private final JwtProvider jwtProvider;

    @PostMapping("/register")
    public RegistrationResponse registerNewsUser(@RequestBody RegistrationRequest request) throws NewsUserValidationException {
        log.info("handling register news user request: " + request);

        if (!RegistrationSource.ONSITE.equals(request.getRegistrationSource())) {
            throw new NewsUserValidationException("Custom registration type of request expected");
        }

        validateRegistrationRequest(request);
        NewsUserDto newsUserDto = requestToDto(request);
        newsUserService.save(newsUserDto);

        String jwt = jwtProvider.generateToken(request.getEmail());
        return new RegistrationResponse(jwt, request.getEmail());
    }

    @PostMapping("/auth")
    public AuthResponse authenticate(@RequestBody AuthRequest authRequest)
            throws NewsUserUnauthorizedException {
        log.info("handling authenticate news user request: " + authRequest);
        NewsUserDto newsUserDto;
        try {
            newsUserDto = newsUserService.findByEmailAndPassword(authRequest.getEmail(), authRequest.getPassword());
        } catch (NewsUserNotFoundException e) {
            throw new NewsUserUnauthorizedException(e.getMessage());
        }

        String jwt = jwtProvider.generateToken(newsUserDto.getEmail());
        return new AuthResponse(jwt, newsUserDto.getId(), newsUserDto.getEmail());
    }

    @GetMapping("/jwt")
    public NewsUserDto getUserByJwt(@RequestHeader(AUTHORIZATION) String jwtWithBearer) throws NewsUserNotFoundException, NewsUserUnauthorizedException {
        log.info("handling getUserByJwt request: " + jwtWithBearer);
        String jwt = getJwtFromString(jwtWithBearer);

        if (jwt != null && jwtProvider.validateToken(jwt)) {
            String email = jwtProvider.getEmailFromToken(jwt);
            return newsUserService.findByEmail(email);
        }
        throw new NewsUserUnauthorizedException("JWT is not correct");
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
    private NewsUserDto requestToDto(RegistrationRequest request) {
        NewsUserDto dto = new NewsUserDto();
        dto.setName(request.getName());
        dto.setEmail(request.getEmail());
        dto.setPassword(request.getPassword());
        dto.setRegistrationSource(request.getRegistrationSource());
        return dto;
    }
}
