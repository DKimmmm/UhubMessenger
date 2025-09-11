package com.example.uhabmessenger.dto.groups;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record GroupCreateDto(

        @NotNull
        @Size(min = 2)
        String title,

        @Nullable
        @Size(min = 5)
        String description,

        List<UUID> userIds

) {
}
