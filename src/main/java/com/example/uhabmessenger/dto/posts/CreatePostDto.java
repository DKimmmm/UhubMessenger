package com.example.uhabmessenger.dto.posts;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreatePostDto(

        @NotNull
        UUID groupOrUserId,

        String title,

        String description

) {
}
