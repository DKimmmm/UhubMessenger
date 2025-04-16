package com.example.UhabMessenger.userdata.repository;

import com.example.UhabMessenger.userdata.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {

    boolean existsByPhone(String username);

    boolean existsByEmail(String username);

    Optional<UserModel> findByEmail(String username);

    Optional<UserModel> findByPhone(String username);

    @Query(value = "DELETE FROM user_images ui WHERE user_id = :userId", nativeQuery = true)
    void deleteByUserId(UUID userId);

    Optional<UserModel> findByUserId(UUID userId);
}
