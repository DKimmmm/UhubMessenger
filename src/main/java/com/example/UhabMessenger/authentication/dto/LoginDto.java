package com.example.UhabMessenger.authentication.dto;

import com.example.UhabMessenger.authentication.validation.PhoneOrEmail;

public record LoginDto(
        @PhoneOrEmail
        String username,
        String password
) {
}
