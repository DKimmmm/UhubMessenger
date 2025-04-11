package com.example.UhabMessenger.user_data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "images")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "image_id")
    private UUID imageId;
    @Column(name = "file_name")
    private String fileName;
    //    private String minioPath;
    @Column(name = "content_type")
    private String contentType;
    private Long size;

}
