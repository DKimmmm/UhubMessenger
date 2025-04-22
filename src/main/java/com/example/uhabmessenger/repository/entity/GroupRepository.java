package com.example.uhabmessenger.repository.entity;

import com.example.uhabmessenger.model.GroupModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GroupRepository extends JpaRepository<GroupModel, UUID> {
    Optional<GroupModel> findByGroupId(UUID uuid);

    boolean existsByTitle(String title);
}
