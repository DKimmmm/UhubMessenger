package com.example.uhabmessenger.exception;

public class UncorrectedPasswordException extends RuntimeException {
    public UncorrectedPasswordException(String message) {
        super(message);
    }
}
