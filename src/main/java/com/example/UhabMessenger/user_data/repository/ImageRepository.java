package com.example.UhabMessenger.user_data.repository;

import com.example.UhabMessenger.authentication.model.UserModel;
import com.example.UhabMessenger.user_data.model.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<ImageModel, UUID> {


    Optional<ImageModel> findByFileName(String fileName);

    Optional<ImageModel> findByUserModel(UserModel user);
}
