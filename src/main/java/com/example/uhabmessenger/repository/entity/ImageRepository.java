package com.example.uhabmessenger.repository.entity;

import com.example.uhabmessenger.model.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImageRepository extends JpaRepository<ImageModel, UUID> {
}
