package com.example.uhabmessenger.dto.user;

import com.example.uhabmessenger.validation.fieldFormat.annotation.EmailFormat;
import com.example.uhabmessenger.validation.fieldFormat.annotation.PhoneFormat;
import com.example.uhabmessenger.validation.fieldFormat.annotation.TitleCase;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UserUpdateInfoDto(

        @NotNull
        UUID userId,

        @Nullable
        @TitleCase
        String name,

        @Nullable
        @TitleCase
        String lastname,

        @Nullable
        @PhoneFormat
        String phone,

        @Nullable
        @EmailFormat
        String email
) {
}
