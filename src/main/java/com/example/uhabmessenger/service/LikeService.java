package com.example.uhabmessenger.service;

import com.example.uhabmessenger.model.CommentModel;
import com.example.uhabmessenger.model.PostModel;
import com.example.uhabmessenger.model.UserModel;
import com.example.uhabmessenger.model.likes.CommentLike;
import com.example.uhabmessenger.model.likes.PostLike;
import com.example.uhabmessenger.repository.entity.CommentLikeRepository;
import com.example.uhabmessenger.repository.entity.CommentRepository;
import com.example.uhabmessenger.repository.entity.PostLikeRepository;
import com.example.uhabmessenger.repository.entity.PostRepository;
import com.example.uhabmessenger.service.post.SimplePostService;
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

//    private final PostRepository postRepository;
    private final SimplePostService simplePostService;

    private final CommentRepository commentRepository;

    private final PostLikeRepository postLikeRepository;

    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    public void addLikeToPost(UUID postId, UUID userId) {

        PostModel postModel = simplePostService.findById(postId);

        UserModel userModel = simpleUserService.findById(userId);

        postModel.addLike(

                PostLike.builder()
                        .post(postModel)
                        .user(userModel)
                        .build()

        );

        simplePostService.save(postModel);

    }

    @Transactional
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

    @Transactional
    public void removeLikeToPost(UUID postId, UUID userId) {

        UserModel user = simpleUserService.findById(userId);

        PostModel post = simplePostService.findById(postId);

        PostLike like = postLikeRepository.findByPostAndUser(post, user).orElseThrow(
                () -> new EntityNotFoundException("post like not found")
        );

        post.removeLike(like);

        simplePostService.save(post);

    }

    @Transactional
    public void removeLikeToComment(UUID commentId, UUID userId) {

        UserModel user = simpleUserService.findById(userId);

        CommentModel comment = commentRepository.findByCommentId(commentId).orElseThrow(
                () -> new EntityNotFoundException("comment not found by " + commentId)
        );

        CommentLike like = commentLikeRepository.findByCommentAndUser(comment, user).orElseThrow(
                () -> new EntityNotFoundException("post like not found")
        );

        comment.removeLike(like);

        commentRepository.save(comment);

    }
}
