package com.example.UhabMessenger.userdata.controller;

import com.example.UhabMessenger.userdata.dto.posts.PostDto;
import com.example.UhabMessenger.userdata.dto.posts.PostInfoDto;
import com.example.UhabMessenger.userdata.dto.user.UserInfoDto;
import com.example.UhabMessenger.userdata.service.ImageService;
import com.example.UhabMessenger.userdata.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> postSave(@RequestParam(value = "title", required = false) String title,
                                      @RequestParam(value = "description", required = false) String description,
                                      @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles) {
        try {
            return ResponseEntity.ok(
                    postService.save(title, description, multipartFiles)
            );
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.valueOf(418));
        }
    }

    @GetMapping("/post-info/{postId}")
    public ResponseEntity<PostInfoDto> getPostInfo(@PathVariable UUID postId) {
        try {
            return ResponseEntity.ok(postService.getPostInfo(postId));
        } catch (Throwable e) {
            return new ResponseEntity<>(HttpStatus.valueOf(414));
        }
    }


}
