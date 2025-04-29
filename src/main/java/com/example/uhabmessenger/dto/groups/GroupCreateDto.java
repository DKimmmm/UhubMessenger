package com.example.uhabmessenger.dto.groups;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record GroupCreateDto(

        @NotNull
        @Size(min = 2)
        String title,
        String description,
        List<UUID> userIds

) {
}
