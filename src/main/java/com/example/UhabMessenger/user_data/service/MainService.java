package com.example.UhabMessenger.user_data.service;

import com.example.UhabMessenger.authentication.model.UserModel;
import com.example.UhabMessenger.authentication.repository.UserRepository;
import com.example.UhabMessenger.user_data.config.MinioInitializer;
import com.example.UhabMessenger.user_data.model.ImageModel;
import com.example.UhabMessenger.user_data.repository.ImageRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MainService {

    private static final Logger log = LoggerFactory.getLogger(MainService.class);
    private final MinioInitializer minioInitializer;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    @SneakyThrows
    public void uploadImage(MultipartFile file, UUID userId) {
        deleteIfAlreadyExists(userId);
        minioInitializer.uploadFile(file.getOriginalFilename(), file.getInputStream(), file.getSize());

        imageSaveInPostgres(file, userId);
    }

    @SneakyThrows
    private void imageSaveInPostgres(MultipartFile file, UUID userId) {

        imageRepository.save(ImageModel.builder()
                .contentType(file.getContentType())
                .size(file.getSize())
                .fileName(file.getOriginalFilename())
                .userModel(findUserById(userId))
                .build());

    }

    @SneakyThrows
    private void deleteIfAlreadyExists(UUID userId) {
        deleteImage(userId);
    }

    private UserModel findUserById(UUID userId) {
        return userRepository.findById(userId).get();
    }

    @SneakyThrows
    public void downloadImage(UUID userId, HttpServletResponse response) {
        ImageModel image = findInPostgres(findUserById(userId));
        if (image == null) {
            return;
        }
        try (InputStream is = minioInitializer.downloadInputStream(image.getFileName()); OutputStream os = response.getOutputStream()) {
            response.setStatus(200);
            response.setContentType(image.getContentType());
            response.setContentLength(image.getSize().intValue());
            is.transferTo(os);
        }

    }

    private ImageModel findInPostgres(UserModel user) {
        try {
            return imageRepository.findByUserModel(user).getFirst().get();
        } catch (Exception e) {
            log.warn("error in find image in postgres");
            return null;
        }
    }

    @SneakyThrows
    public void deleteImage(UUID userId) {
        try {
            ImageModel imageModel = imageRepository.findByUserModel(findUserById(userId)).getFirst().get();
            String fileName = imageModel.getFileName();
            minioInitializer.deleteFile(fileName);
            imageRepository.delete(imageModel);
        } catch (Throwable e) {
            log.info("file not found or delete error");
        }
    }
}
