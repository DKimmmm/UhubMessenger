package com.example.uhabmessenger.dto.posts;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public record CreatePostDto(

        UUID groupOrUserId,

        String title,

        String description

//        List<MultipartFile> multipartFiles

) {
}
