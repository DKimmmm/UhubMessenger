package com.example.UhabMessenger.userdata.dto.register;

import com.example.UhabMessenger.userdata.validation.Password;
import com.example.UhabMessenger.userdata.validation.PhoneOrEmail;
import jakarta.validation.Valid;

@Valid
public record LoginDto(
        @PhoneOrEmail
        String username,
        @Password
        String password
) {
}
