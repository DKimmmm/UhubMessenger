package com.example.uhabmessenger.service;

import com.example.uhabmessenger.model.CommentModel;
import com.example.uhabmessenger.model.PostModel;
import com.example.uhabmessenger.model.UserModel;
import com.example.uhabmessenger.model.likes.CommentLike;
import com.example.uhabmessenger.model.likes.PostLike;
import com.example.uhabmessenger.repository.entity.CommentRepository;
import com.example.uhabmessenger.repository.entity.PostRepository;
import com.example.uhabmessenger.service.user.other.SimpleUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final SimpleUserService simpleUserService;

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void addLikeToPost(UUID postId, UUID userId) {

        PostModel postModel = postRepository.findByPostId(postId).orElseThrow(
                () -> new EntityNotFoundException("post not found by " + postId.toString())
        );

        UserModel userModel = simpleUserService.findById(userId);

        postModel.addLike(

                PostLike.builder()
                        .post(postModel)
                        .user(userModel)
                        .build()

        );

        postRepository.save(postModel);

    }

    public void addLikeToComment(UUID commentId, UUID userId) {

        CommentModel commentModel = commentRepository.findByCommentId(commentId).orElseThrow(
                () -> new EntityNotFoundException("comment not found by " + commentId.toString())
        );

        UserModel userModel = simpleUserService.findById(userId);

        commentModel.addLike(

                CommentLike.builder()
                        .comment(commentModel)
                        .user(userModel)
                        .build()

        );

        commentRepository.save(commentModel);

    }
}
