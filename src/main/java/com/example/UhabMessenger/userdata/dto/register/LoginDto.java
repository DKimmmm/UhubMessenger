package com.example.UhabMessenger.userdata.dto.register;

import com.example.UhabMessenger.authentication.validation.Password;
import com.example.UhabMessenger.authentication.validation.PhoneOrEmail;
import jakarta.validation.Valid;

@Valid
public record LoginDto(
        @PhoneOrEmail
        String username,
        @Password
        String password
) {
}
