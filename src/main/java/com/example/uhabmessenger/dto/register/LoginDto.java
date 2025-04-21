package com.example.uhabmessenger.dto.register;

import com.example.uhabmessenger.validation.Password;
import com.example.uhabmessenger.validation.PhoneOrEmail;
import jakarta.validation.Valid;

@Valid
public record LoginDto(
        @PhoneOrEmail
        String username,
        @Password
        String password
) {
}
