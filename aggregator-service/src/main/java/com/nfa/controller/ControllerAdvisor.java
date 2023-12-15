package com.nfa.controller;

import com.nfa.dto.ErrorDto;
import com.nfa.exception.UserServiceUnavailableException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(value = {UserServiceUnavailableException.class})
    public ResponseEntity<ErrorDto> handleUserServiceUnavailableException(UserServiceUnavailableException exception) {
        return ResponseEntity.badRequest().body(composeErrorDto(exception));
    }

    private ErrorDto composeErrorDto(RuntimeException exception) {
       return new ErrorDto(exception.getMessage(),LocalDateTime.now());
    }

}
