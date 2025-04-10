package com.example.UhabMessenger.authentication.exception;

public class UncorrectedPasswordException extends RuntimeException {
    public UncorrectedPasswordException(String message) {
        super(message);
    }
}
