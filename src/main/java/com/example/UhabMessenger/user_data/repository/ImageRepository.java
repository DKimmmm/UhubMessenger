package com.example.UhabMessenger.user_data.repository;

import com.example.UhabMessenger.authentication.model.UserModel;
import com.example.UhabMessenger.user_data.model.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<ImageModel, UUID> {

    @Query(value = "SELECT i.image_id, i.content_type, i.file_name, i.\"size\" FROM images i JOIN user_images ui ON i.image_id = ui.image_id WHERE ui.user_id = :userId", nativeQuery = true)
    List<ImageModel> findByUserId(@Param("userId")UUID userId);

}
