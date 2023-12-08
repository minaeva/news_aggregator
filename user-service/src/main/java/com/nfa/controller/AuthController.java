package com.nfa.controller;

import com.nfa.config.jwt.JwtProvider;
import com.nfa.controller.request.AuthRequest;
import com.nfa.controller.request.RegistrationRequest;
import com.nfa.controller.response.AuthResponse;
import com.nfa.controller.response.RegistrationResponse;
import com.nfa.dto.ReaderDto;
import com.nfa.entity.ReaderRole;
import com.nfa.entity.RegistrationSource;
import com.nfa.exception.ReaderNotFoundException;
import com.nfa.exception.ReaderUnauthorizedException;
import com.nfa.exception.ReaderValidationException;
import com.nfa.service.ReaderService;
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

    private final ReaderService readerService;
    private final JwtProvider jwtProvider;

    @PostMapping("/registration")
    public RegistrationResponse registerNewsUser(@RequestBody RegistrationRequest request) {
        log.info("handling register news user request: " + request);

        if (!RegistrationSource.ONSITE.equals(request.getRegistrationSource())) {
            throw new ReaderValidationException("Custom registration type of request expected");
        }

        validateRegistrationRequest(request);
        ReaderDto readerDto = toReaderDto(request);
        readerService.save(readerDto);

        String jwt = jwtProvider.generateToken(request.getEmail());
        return new RegistrationResponse(jwt, request.getEmail());
    }

    @PostMapping("/auth")
    public AuthResponse authenticate(@RequestBody AuthRequest authRequest) {
        log.info("handling authenticate news user request: " + authRequest);
        ReaderDto readerDto;
        try {
            readerDto = readerService.findByEmailAndPassword(authRequest.getEmail(), authRequest.getPassword());
        } catch (ReaderNotFoundException e) {
            throw new ReaderUnauthorizedException(e.getMessage());
        }

        String jwt = jwtProvider.generateToken(readerDto.email());
        return new AuthResponse(jwt, readerDto.id(), readerDto.email());
    }

    @GetMapping("/jwt")
    public ReaderDto getUserByJwt(@RequestHeader(AUTHORIZATION) String jwtWithBearer) {
        log.info("handling getUserByJwt request: " + jwtWithBearer);
        String jwt = getJwtFromString(jwtWithBearer);

        if (jwt != null && jwtProvider.validateToken(jwt)) {
            String email = jwtProvider.getEmailFromToken(jwt);
            return readerService.findByEmail(email);
        }
        throw new ReaderUnauthorizedException("JWT is not correct");
    }

    private ReaderDto toReaderDto(RegistrationRequest request) {
        return new ReaderDto(null, request.getName(), request.getPassword(),
                request.getEmail(), ReaderRole.READER_ROLE, request.getRegistrationSource());
    }
}
