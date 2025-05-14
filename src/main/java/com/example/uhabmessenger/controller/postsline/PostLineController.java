package com.example.uhabmessenger.controller.postsline;

import com.example.uhabmessenger.dto.posts.PostWithLikesInfoDto;
import com.example.uhabmessenger.service.post.PostLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post-line")
public class PostLineController {

    private final PostLineService postLineService;

    @GetMapping("/post-info/all")
    public ResponseEntity<List<PostWithLikesInfoDto>> getAllPostInfoList() {

        return ResponseEntity.ok(
                postLineService.getAllPostInfo()
        );

    }

}
