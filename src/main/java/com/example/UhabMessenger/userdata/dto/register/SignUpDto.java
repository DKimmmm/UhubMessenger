package com.example.UhabMessenger.userdata.dto.register;

import com.example.UhabMessenger.userdata.validation.Password;
import com.example.UhabMessenger.userdata.validation.PhoneOrEmail;
import com.example.UhabMessenger.userdata.validation.TitleCase;
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
