package com.example.UhabMessenger.authentication.dto;

import com.example.UhabMessenger.authentication.validation.PhoneOrEmail;
import jakarta.validation.constraints.NotNull;

public record SignUpDto(
        @NotNull
        String name,
        String lastname,
        @PhoneOrEmail
        String username,
        String password
) {
}
