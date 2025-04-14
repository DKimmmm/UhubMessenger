package com.example.UhabMessenger.authentication.repository;

import com.example.UhabMessenger.authentication.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {

    boolean existsByPhone(String username);

    boolean existsByEmail(String username);

    Optional<UserModel> findByEmail(String username);

    Optional<UserModel> findByPhone(String username);
}
