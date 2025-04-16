package com.example.UhabMessenger.userdata.controller;

import com.example.UhabMessenger.userdata.dto.posts.PostDto;
import com.example.UhabMessenger.userdata.service.ImageService;
import com.example.UhabMessenger.userdata.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;


    @PostMapping
    public ResponseEntity<?> postSave(@RequestBody PostDto postDto) {
        try {
            postService.save(postDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.valueOf(418));
        }
    }

    @SneakyThrows
    @PostMapping(value = "/create/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> save(@PathVariable UUID postId,
                                  @RequestPart() MultipartFile multipartFile) {
        postService.uploadPostImage(multipartFile, postId);
        return ResponseEntity.ok().build();
    }

}
