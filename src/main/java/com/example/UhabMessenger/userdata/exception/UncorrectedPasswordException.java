package com.example.UhabMessenger.userdata.exception;

public class UncorrectedPasswordException extends RuntimeException {
    public UncorrectedPasswordException(String message) {
        super(message);
    }
}
