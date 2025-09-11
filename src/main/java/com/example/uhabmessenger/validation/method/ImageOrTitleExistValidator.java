package com.example.uhabmessenger.validation.method;

import com.example.uhabmessenger.dto.posts.CreatePostDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;
import jakarta.validation.constraintvalidation.ValidationTarget;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class ImageOrTitleExistValidator implements ConstraintValidator<ImageOrTitleExist, Object[]> {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("multipart/form-data", "image/png", "image/jpeg");
    private static final Set<String> ALLOWED_ORIGINAL_NAMES = Set.of("png", "jpg", "jpeg");


    @Override
    public boolean isValid(Object[] args, ConstraintValidatorContext context) {

        if (Objects.equals(args, null) || args.length != 2) {
            return false;
        }


        List<MultipartFile> multipartFiles = getMultipartFiles(args);

        CreatePostDto createPostDto;

        if (Objects.nonNull(args[1])) {
            if (!(args[1] instanceof CreatePostDto)) {
                throw new IllegalArgumentException(
                        "Illegal method signature, expected parameter of type CreatePostDto");
            }

            try {
                createPostDto = (CreatePostDto) args[1];
            } catch (Exception e) {
                throw new IllegalArgumentException("arguments of wrong types");
            }

        } else {
            return false;
        }

        boolean hasTitle = Objects.nonNull(createPostDto.title()) && !(createPostDto.title().isBlank()) && createPostDto.title().trim().length() > 3;
        boolean hasValidImages = multipartFiles != null && !multipartFiles.isEmpty() && validateImages(multipartFiles);

        return hasTitle || hasValidImages;
    }

    private static @Nullable List<MultipartFile> getMultipartFiles(Object[] args) {

        if (Objects.nonNull(args[0])) {

            if (!(args[0] instanceof List<?>)) {
                throw new IllegalArgumentException(
                        "Illegal method signature, expected parameter of type List<MultipartFile>");
            }

            try {
                return (List<MultipartFile>) args[0];
            } catch (Exception e) {
                throw new IllegalArgumentException("arguments of wrong types");
            }

        }

        return null;

    }

    private boolean validateImages(List<MultipartFile> multipartFiles) {

        for (MultipartFile file : multipartFiles) {

            if (file == null || file.isEmpty()) {
                return false;
            }


            String contentType = file.getContentType();
            if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
                log.warn("Invalid content type: {}", contentType);
                return false;
            }


            String originalFilename = file.getOriginalFilename();
            if (originalFilename != null && !originalFilename.isEmpty()) {
                String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
                if (!ALLOWED_ORIGINAL_NAMES.contains(extension)) {
                    log.warn("Invalid file extension: {}", extension);
                    return false;
                }
            } else {
                log.warn("Filename is empty or null");
                return false;
            }

        }

        return true;
    }
}
