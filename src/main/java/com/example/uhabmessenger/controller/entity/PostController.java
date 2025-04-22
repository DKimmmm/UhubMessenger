package com.example.uhabmessenger.controller.entity;

import com.example.uhabmessenger.dto.posts.PostInfoDto;
import com.example.uhabmessenger.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postSave(@RequestParam(value = "userId") UUID userId,
                                      @RequestParam(value = "title", required = false) String title,
                                      @RequestParam(value = "description", required = false) String description,
                                      @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles) {

            postService.save(userId, title, description, multipartFiles);
            return ResponseEntity.ok().build();

    }

    @GetMapping("/post-info/{postId}")
    public ResponseEntity<PostInfoDto> getPostInfo(@PathVariable UUID postId) {

            return ResponseEntity.ok(postService.getPostInfo(postId));

    }


}
