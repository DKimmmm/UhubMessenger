package com.example.UhabMessenger.authentication.repository;

import com.example.UhabMessenger.authentication.model.UserModel;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    boolean existsByPhone(String username);

    boolean existsByEmail(String username);

    Optional<UserModel> findByEmail(String username);
    Optional<UserModel> findByPhone(String username);
}
