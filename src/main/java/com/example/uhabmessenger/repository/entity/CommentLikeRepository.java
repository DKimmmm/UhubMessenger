package com.example.uhabmessenger.repository.entity;

import com.example.uhabmessenger.model.UserModel;
import com.example.uhabmessenger.model.likes.CommentLike;
import com.example.uhabmessenger.model.likes.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CommentLikeRepository extends JpaRepository<CommentLike, UUID> {

    Optional<CommentLike> findByLikeId(UUID likeId);

    Optional<CommentLike> findByUser(UserModel model);

}
