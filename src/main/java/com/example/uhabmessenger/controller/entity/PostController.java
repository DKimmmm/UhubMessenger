package com.example.uhabmessenger.controller.entity;

import com.example.uhabmessenger.dto.comment.AddCommentDto;
import com.example.uhabmessenger.dto.comment.CommentInfoDto;
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

    @PostMapping(value = "/user/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> userPostSave(@RequestParam(value = "userId") UUID userId,
                                             @RequestParam(value = "title", required = false) String title,
                                             @RequestParam(value = "description", required = false) String description,
                                             @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles) {

        postService.userPostSave(userId, title, description, multipartFiles);
        return ResponseEntity.ok().build();

    }

    @PostMapping(value = "/group/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> groupPostSave(@RequestParam(value = "groupId") UUID groupId,
                                              @RequestParam(value = "title", required = false) String title,
                                              @RequestParam(value = "description", required = false) String description,
                                              @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles) {

        postService.groupPostSave(groupId, title, description, multipartFiles);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/post-info/{postId}")
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
}
