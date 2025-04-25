package com.example.uhabmessenger.dto.comment;

import java.util.UUID;

public record AddCommentDto(

        UUID userId,

        UUID postId,

        String text

) {
}