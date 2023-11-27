package com.nfa.controller;

import com.nfa.exception.KeywordNotFoundException;
import com.nfa.exception.ReaderNotFoundException;
import com.nfa.exception.ReaderUnauthorizedException;
import com.nfa.exception.ReaderValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(value = {KeywordNotFoundException.class})
    public ResponseEntity<ErrorDto> handleNotFoundException(KeywordNotFoundException exception) {
        return ResponseEntity.badRequest().body(ErrorDto.builder()
                .message(exception.getMessage())
                .time(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(value = {ReaderNotFoundException.class})
    public ResponseEntity<ErrorDto> handleReaderNotFoundException(ReaderNotFoundException exception) {
        return ResponseEntity.badRequest().body(ErrorDto.builder()
                .message(exception.getMessage())
                .time(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(value = {ReaderUnauthorizedException.class})
    public ResponseEntity<ErrorDto> handleReaderUnauthorizedException(ReaderUnauthorizedException exception) {
        return ResponseEntity.badRequest().body(ErrorDto.builder()
                .message(exception.getMessage())
                .time(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(value = {ReaderValidationException.class})
    public ResponseEntity<ErrorDto> handleReaderValidationException(ReaderValidationException exception) {
        return ResponseEntity.badRequest().body(ErrorDto.builder()
                .message(exception.getMessage())
                .time(LocalDateTime.now())
                .build());
    }
}
