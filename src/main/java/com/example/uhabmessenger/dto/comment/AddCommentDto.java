package com.example.uhabmessenger.dto.comment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AddCommentDto(

        @NotNull
        UUID userId,

        @NotNull
        UUID postId,

        @NotNull
        @Size(min = 2)
        String text

) {
}