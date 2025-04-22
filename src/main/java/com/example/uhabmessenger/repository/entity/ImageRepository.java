package com.example.uhabmessenger.repository.entity;

import com.example.uhabmessenger.model.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<ImageModel, UUID> {

    @Query(value = "SELECT i.file_name FROM images i JOIN post_images pi ON i.image_id = pi.image_id WHERE pi.post_id = :postId", nativeQuery = true)
    List<String> findFileNamesByPostId(UUID postId);

    @Query(value = "SELECT i.file_name FROM images i JOIN user_images ui ON i.image_id = ui.image_id WHERE ui.user_id = :userId", nativeQuery = true)
    List<String> findFileNamesByUserId(UUID userId);
}
