package com.example.uhabmessenger.dto.user;

import com.example.uhabmessenger.validation.fieldFormat.annotation.EmailFormat;
import com.example.uhabmessenger.validation.fieldFormat.annotation.PhoneFormat;
import com.example.uhabmessenger.validation.fieldFormat.annotation.TitleCase;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UserUpdateInfoDto(

        @NotNull
        UUID userId,

        @TitleCase
        String name,

        @TitleCase
        String lastname,

        @PhoneFormat
        String phone,

        @EmailFormat
        String email

) {
}
