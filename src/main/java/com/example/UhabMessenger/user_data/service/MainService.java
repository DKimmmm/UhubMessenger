package com.example.UhabMessenger.user_data.service;

import com.example.UhabMessenger.user_data.config.MinioInitializer;
import com.example.UhabMessenger.user_data.model.ImageModel;
import com.example.UhabMessenger.user_data.repository.ImageRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;

@Service
@RequiredArgsConstructor
public class MainService {

    private static final Logger log = LoggerFactory.getLogger(MainService.class);
    private final MinioInitializer minioInitializer;
    private final ImageRepository imageRepository;

    public void uploadImage(MultipartFile file) throws Throwable {
        minioInitializer.uploadFile(file.getOriginalFilename(), file.getInputStream(), file.getSize());

        imageSaveInPostgres(file);
        log.info("дописать сохраннение в postgresql");
    }

    private void imageSaveInPostgres(MultipartFile file) {
        imageRepository.save(
                ImageModel.builder()
                        .contentType(file.getContentType())
                        .size(file.getSize())
                        .fileName(file.getOriginalFilename()).build());
    }

    public void downloadImage(String fileName, HttpServletResponse response) throws Throwable {
        ImageModel image = findInPostgres(fileName);
        if (image == null) {
            return;
        }
        log.info("---------------> -_----------------> image name {}", image.getFileName());
        try (InputStream is = minioInitializer.downloadInputStream(fileName);
             OutputStream os = response.getOutputStream()) {
            response.setStatus(200);
            response.setContentType(image.getContentType());
            response.setContentLength(image.getSize().intValue());
            is.transferTo(os);
        }

    }

    private ImageModel findInPostgres(String fileName) {
        try {
            return imageRepository.findByFileName(fileName).get();
        } catch (Exception e) {
            log.warn("error in find image in postgres");
            return null;
        }
    }
}
