package com.example.uhabmessenger.controller.entity;

import com.example.uhabmessenger.dto.comment.AddCommentDto;
import com.example.uhabmessenger.dto.comment.CommentInfoDto;
import com.example.uhabmessenger.dto.posts.CreatePostDto;
import com.example.uhabmessenger.dto.posts.PostInfoDto;
import com.example.uhabmessenger.service.PostService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @PostMapping(value = "/user/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> userPostSave(@RequestPart(name = "images") List<MultipartFile> multipartFiles,
                                             @RequestPart(name = "dto") CreatePostDto createPostDto) {

        log.info("check---------------------------------------");
        postService.userPostSave(createPostDto, multipartFiles);
        return ResponseEntity.ok().build();

    }

    @PostMapping(value = "/group/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> groupPostSave(@RequestPart("dto") CreatePostDto createPostDto,
                                              @RequestPart("images") List<MultipartFile> multipartFiles) {

        postService.groupPostSave(createPostDto, multipartFiles);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/info/{postId}")
    public ResponseEntity<PostInfoDto> getPostInfo(@PathVariable UUID postId) {

        return ResponseEntity.ok(postService.getPostInfo(postId));

    }

    @PostMapping("/add/comment")
    public ResponseEntity<Void> addComment(@RequestBody AddCommentDto addCommentDto) {

        postService.addComment(addCommentDto);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/get-comments")
    public ResponseEntity<List<CommentInfoDto>> getInfoByPostId(@RequestParam UUID postId) {

        return ResponseEntity.ok(
                postService.getCommentsByPostId(postId)
        );

    }

    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Void> imageDownload(@PathVariable UUID imageId, HttpServletResponse response) {

        postService.imageDownload(imageId, response);
        return ResponseEntity.ok().build();

    }
}
