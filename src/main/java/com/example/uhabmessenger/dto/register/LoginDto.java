package com.example.uhabmessenger.dto.register;

import com.example.uhabmessenger.validation.annotation.Password;
import com.example.uhabmessenger.validation.annotation.PhoneOrEmail;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Valid
public record LoginDto(

        @PhoneOrEmail
        @NotNull
        String username,

        @Password
        @NotNull
        String password

) {
}
