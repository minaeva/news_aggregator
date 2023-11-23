package com.nfa.exception;

public class ReaderNotFoundException extends RuntimeException {

    public ReaderNotFoundException() {
        super();
    }

    public ReaderNotFoundException(String message) {
        super(message);
    }
}
