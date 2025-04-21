package com.example.UhabMessenger.userdata.repository.entity;

import com.example.UhabMessenger.userdata.model.GroupModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GroupRepository extends JpaRepository<GroupModel, UUID> {
    Optional<GroupModel> findByGroupId(UUID uuid);
}
