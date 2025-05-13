package com.example.uhabmessenger.repository.entity;

import com.example.uhabmessenger.model.CommentModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<CommentModel, UUID> {

    Optional<CommentModel> findByCommentId(UUID commentId);

}
