package com.example.uhabmessenger.controller;

import com.example.uhabmessenger.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/to-post")
    public ResponseEntity<Void> addLikeToPost(@RequestParam UUID postId,
                              @RequestParam UUID userId) {

        likeService.addLikeToPost(postId, userId);
        return ResponseEntity.ok().build();

    }

    @PostMapping("/to-comment")
    public ResponseEntity<Void> addLikeToComment(@RequestParam UUID commentId,
                                 @RequestParam UUID userId) {

        likeService.addLikeToComment(commentId, userId);
        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/to-post")
    public ResponseEntity<Void> removeLikeToPost(@RequestParam UUID postId,
                          @RequestParam UUID userId) {

        likeService.removeLikeToPost(postId, userId);
        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/to-comment")
    public ResponseEntity<Void> removeLikeToComment(@RequestParam UUID commentId,
                                 @RequestParam UUID userId) {

        likeService.removeLikeToComment(commentId, userId);
        return ResponseEntity.ok().build();

    }


}
