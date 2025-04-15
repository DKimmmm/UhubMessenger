package com.example.UhabMessenger.userdata.service.user.main;

import com.example.UhabMessenger.authentication.exception.AuthorizationErrorException;
import com.example.UhabMessenger.authentication.exception.UncorrectedPasswordException;
import com.example.UhabMessenger.userdata.config.MinioInitializer;
import com.example.UhabMessenger.userdata.model.ImageModel;
import com.example.UhabMessenger.userdata.model.PostModel;
import com.example.UhabMessenger.userdata.model.UserModel;
import com.example.UhabMessenger.authentication.repository.UserRepository;
import com.example.UhabMessenger.userdata.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final MinioInitializer minioInitializer;

    public UserModel getUserByUsername(String username) {
        if (usernameIsEmailFormat(username)) {
            return findUserByEmail(username);
        } else if (usernameIsPhoneFormat(username)) {
            return findUserByPhone(username);
        } else {
            throw new AuthorizationErrorException("username is not correct format");
        }
    }

    private UserModel findUserByEmail(String username) {
        try {
            return userRepository.findByEmail(username).get();
        } catch (Exception e) {
            throw new UncorrectedPasswordException("uncorrected password");
        }
    }
    private UserModel findUserByPhone(String username) {
        return userRepository.findByPhone(username).get();
    }

    // Проверка, соответствует ли username формату номера телефона
    private boolean usernameIsPhoneFormat(String username) {
        if (username == null || username.trim().isEmpty()) {
            log.info("Username is null or empty, cannot validate as phone format");
            return false;
        }

        // Регулярное выражение для номера телефона
        // Поддерживает: +12345678901, +1 (123) 456-7890, +7 999 123-45-67, и т.д.
        String phoneRegex = "^\\+?[1-9]\\d{1,14}([\\s-]?\\d{2,4})*$|^\\+?[1-9]\\d{1,14}(\\(\\d{1,4}\\))?([\\s-]?\\d{2,4})*$";

        boolean isPhoneFormat = username.matches(phoneRegex);
        log.info("Checking if username '{}' is in phone format: {}", username, isPhoneFormat);
        return isPhoneFormat;
    }

    // Проверка, соответствует ли username формату email
    private boolean usernameIsEmailFormat(String username) {
        if (username == null || username.trim().isEmpty()) {
            log.info("Username is null or empty, cannot validate as email format");
            return false;
        }

        // Регулярное выражение для email
        // Поддерживает: user@domain.com, user.name@sub.domain.co, и т.д.
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        boolean isEmailFormat = username.matches(emailRegex);
        log.info("Checking if username '{}' is in email format: {}", username, isEmailFormat);
        return isEmailFormat;
    }

    public void uploadUserImage(MultipartFile multipartFile, UUID userId) {
        deleteIfAlreadyExists(userId);
        ImageModel imageModel = imageService.uploadImage(multipartFile);
        imageSaveInPostgres(userId, imageModel);
    }

    private void imageSaveInPostgres(UUID userId, ImageModel imageModel) {
        UserModel userModel = userRepository.findById(userId).get();
        Set<ImageModel> images = userModel.getImages();
        images.add(imageModel);
        userRepository.save(userModel);
    }

    @SneakyThrows
    private void deleteIfAlreadyExists(UUID userId) {
        try {
            deleteFromMinio(userId);
            deleteFromPostImageTable(userId);
        } catch (Throwable e) {
            log.info("file not found or delete error");
        }
    }
    private void deleteFromPostImageTable(UUID userId) {
        userRepository.deleteByUserId(userId);
        log.info("delete from post_images repository by postId: {}", userId);
    }

    private void deleteFromMinio(UUID userId) {
        List<String> fileNames = imageService.findByUserId(userId);
        for (String fileName : fileNames) {
            minioInitializer.deleteFile(fileName);
            log.info("minio delete by filename: {}", fileName);
        }
    }


}
