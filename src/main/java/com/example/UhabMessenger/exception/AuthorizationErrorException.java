package com.example.UhabMessenger.exception;

public class AuthorizationErrorException extends RuntimeException {
    public AuthorizationErrorException(String message) {
        super(message);
    }
}
