package com.example.uhabmessenger.repository.entity;

import com.example.uhabmessenger.model.UserModel;
import com.example.uhabmessenger.model.likes.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PostLikeRepository extends JpaRepository<PostLike, UUID> {

    Optional<PostLike> findByPostLikeId(UUID postLikeId);
    Optional<PostLike> findByUser(UserModel model);

}
