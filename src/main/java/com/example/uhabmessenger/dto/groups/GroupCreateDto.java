package com.example.uhabmessenger.dto.groups;

import com.example.uhabmessenger.validation.annotation.TitleCase;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record GroupCreateDto(

        @NotNull
        @TitleCase
        @Min(2)
        String title,
        String description,
        List<UUID> userIds

) {
}
