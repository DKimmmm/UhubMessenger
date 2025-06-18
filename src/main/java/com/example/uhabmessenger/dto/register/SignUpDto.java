package com.example.uhabmessenger.dto.register;

import com.example.uhabmessenger.validation.fieldFormat.annotation.Password;
import com.example.uhabmessenger.validation.fieldFormat.annotation.PhoneOrEmail;
import com.example.uhabmessenger.validation.fieldFormat.annotation.TitleCase;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SignUpDto(

        @NotNull
        @Size(min = 2)
        @TitleCase
        String name,

        @Size(min = 2)
        @TitleCase
        @NotNull
        String lastname,

        @PhoneOrEmail
        @Size(min = 10)
        @NotNull
        String username,

        @Password
        @NotNull
        @Size(min = 8)
        String password

) {
}
