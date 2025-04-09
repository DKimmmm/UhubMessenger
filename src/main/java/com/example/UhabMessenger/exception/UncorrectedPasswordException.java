package com.example.UhabMessenger.exception;

public class UncorrectedPasswordException extends RuntimeException {
    public UncorrectedPasswordException(String message) {
        super(message);
    }
}
