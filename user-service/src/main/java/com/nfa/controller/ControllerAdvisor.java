package com.nfa.controller;

import com.nfa.dto.ErrorDto;
import com.nfa.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorDto> handleNotFoundException(MethodArgumentNotValidException exception) {
        ErrorDto error = new ErrorDto(exception.getMessage(),LocalDateTime.now());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(value = {RequestValidationException.class})
    public ResponseEntity<ErrorDto> handleRequestValidationException(RequestValidationException exception) {
        return ResponseEntity.badRequest().body(composeErrorDto(exception));
    }

    @ExceptionHandler(value = {KeywordNotFoundException.class})
    public ResponseEntity<ErrorDto> handleKeywordNotFoundException(KeywordNotFoundException exception) {
        return ResponseEntity.badRequest().body(composeErrorDto(exception));
    }

    @ExceptionHandler(value = {ReaderNotFoundException.class})
    public ResponseEntity<ErrorDto> handleReaderNotFoundException(ReaderNotFoundException exception) {
        return ResponseEntity.badRequest().body(composeErrorDto(exception));

    }

    @ExceptionHandler(value = {ReaderUnauthorizedException.class})
    public ResponseEntity<ErrorDto> handleReaderUnauthorizedException(ReaderUnauthorizedException exception) {
        return ResponseEntity.badRequest().body(composeErrorDto(exception));

    }

    @ExceptionHandler(value = {ReaderValidationException.class})
    public ResponseEntity<ErrorDto> handleReaderValidationException(ReaderValidationException exception) {
        return ResponseEntity.badRequest().body(composeErrorDto(exception));
    }

    private ErrorDto composeErrorDto(RuntimeException exception) {
       return new ErrorDto(exception.getMessage(),LocalDateTime.now());
    }

}
