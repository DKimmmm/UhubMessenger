package com.example.uhabmessenger.dto.register;

import com.example.uhabmessenger.validation.Password;
import com.example.uhabmessenger.validation.PhoneOrEmail;
import com.example.uhabmessenger.validation.TitleCase;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SignUpDto(
        @NotNull
        @Size(min = 2)
        @TitleCase
        String name,

        @Size(min = 2)
        @TitleCase
        String lastname,

        @PhoneOrEmail
        @Size(min = 10)
        String username,

        @Password
        @Size(min = 8)
        String password
) {
}
