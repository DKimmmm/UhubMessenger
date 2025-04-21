package com.example.uhabmessenger.repository.entity;

import com.example.uhabmessenger.model.PostModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<PostModel, UUID> {
    @Query(value = "DELETE FROM post_images pi WHERE post_id = :postId", nativeQuery = true)
    void deleteAttachByPostId(UUID postId);


    Optional<PostModel> findByPostId(UUID postId);
}
