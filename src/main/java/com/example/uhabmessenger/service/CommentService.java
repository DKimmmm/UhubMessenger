package com.example.uhabmessenger.service;

import com.example.uhabmessenger.dto.comment.CommentInfoDto;
import com.example.uhabmessenger.model.CommentModel;
import com.example.uhabmessenger.model.PostModel;
import com.example.uhabmessenger.service.post.SimplePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final SimplePostService simplePostService;

    @Transactional(readOnly = true)
    public List<CommentInfoDto> getCommentsByPostId(UUID postId) {

        PostModel postModel = simplePostService.findById(postId);

        return listCommentsToListInfoDto(postModel.getComments());

    }


    private List<CommentInfoDto> listCommentsToListInfoDto(List<CommentModel> comments) {

        return comments.stream()
                .map(comment -> CommentInfoDto.builder()
                        .commentId(comment.getCommentId())
                        .text(comment.getText())
                        .userId(comment.getUser().getUserId())
                        .userName(comment.getUser().getName())
                        .userLastname(comment.getUser().getLastname())
                        .build())
                .toList();

    }

}
