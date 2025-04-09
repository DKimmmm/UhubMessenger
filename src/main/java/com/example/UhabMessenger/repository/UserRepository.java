package com.example.UhabMessenger.repository;

import com.example.UhabMessenger.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    boolean existsByPhone(String username);

    boolean existsByEmail(String username);
}
