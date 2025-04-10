package com.example.UhabMessenger.authentication.dto;

public record SignUpDto(
        String name,
        String lastname,
        String username,
        String password
) {
}
