package com.example.UhabMessenger.userdata.service;

import com.example.UhabMessenger.userdata.model.UserModel;
import com.example.UhabMessenger.userdata.repository.UserRepository;
import com.example.UhabMessenger.userdata.config.MinioInitializer;
import com.example.UhabMessenger.userdata.model.ImageModel;
import com.example.UhabMessenger.userdata.repository.ImageRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private static final Logger log = LoggerFactory.getLogger(ImageService.class);
    private final MinioInitializer minioInitializer;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    @SneakyThrows
    public ImageModel uploadImage(MultipartFile file) {
        minioInitializer.uploadFile(
                file.getOriginalFilename(),
                file.getInputStream(),
                file.getSize()
        );

        return imageModelBuilder(file);
    }

    @SneakyThrows
    private ImageModel imageModelBuilder(MultipartFile file) {

        return ImageModel.builder()
                .contentType(file.getContentType())
                .size(file.getSize())
                .fileName(file.getOriginalFilename())
                .build();

    }

//    @SneakyThrows
//    private void deleteIfAlreadyExists(UUID userId) {
//        deleteImage(userId);
//    }

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
//            return imageRepository.findByUserModel(user).getFirst().get();
            log.info("---------------------------------------------- переделай поиск по юзеру");
            List<ImageModel> allByUserId = imageRepository.findByUserId(user.getUserId());
            log.info("list = {}", allByUserId.toString());
            throw new RuntimeException();
        } catch (Exception e) {
            log.warn("error in find image in postgres");
            return null;
        }
    }

    public List<String> findByPostId(UUID postId) {
        return imageRepository.findFileNamesByPostId(postId);
    }

    @SneakyThrows
    public void deleteImage(UUID userId) {
        try {
            log.info("---------------------------------------------- переделай delete по юзеру");
            throw new RuntimeException();
//            ImageModel imageModel = imageRepository.findByUserModel(findUserById(userId)).getFirst().get();
//            String fileName = imageModel.getFileName();
//            minioInitializer.deleteFile(fileName);
//            imageRepository.delete(imageModel);
        } catch (Throwable e) {
            log.info("file not found or delete error");
        }
    }

    public List<String> findByUserId(UUID userId) {
        return imageRepository.findFileNamesByUserId(userId);
    }

}
