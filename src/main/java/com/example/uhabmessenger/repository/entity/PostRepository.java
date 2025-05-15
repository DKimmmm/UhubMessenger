package com.example.uhabmessenger.repository.entity;

import com.example.uhabmessenger.model.PostModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<PostModel, UUID> {

    Optional<PostModel> findByPostId(UUID postId);

    List<PostModel> findAllByRemoveMark(boolean removeMark);
}
