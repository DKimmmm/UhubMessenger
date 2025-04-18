package com.example.UhabMessenger.userdata.exception;

public class AuthorizationErrorException extends RuntimeException {
    public AuthorizationErrorException(String message) {
        super(message);
    }
}
