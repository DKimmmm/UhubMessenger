package com.example.uhabmessenger.service;

import com.example.uhabmessenger.exception.DownloadImageException;
import com.example.uhabmessenger.model.ImageModel;
import com.example.uhabmessenger.repository.MinioService;
import com.example.uhabmessenger.repository.entity.ImageRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final MinioService minioService;
    private final ImageRepository imageRepository;

    @SneakyThrows
    public ImageModel uploadImage(MultipartFile file) {
        minioService.uploadFile(
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

    public List<String> findByPostId(UUID postId) {
        return imageRepository.findFileNamesByPostId(postId);
    }

    public List<String> findByUserId(UUID userId) {
        return imageRepository.findFileNamesByUserId(userId);
    }

    public ImageModel findByImageId(UUID imageId) {
        return imageRepository.findById(imageId).get();
    }

    public void deleteFromMinio(String fileName) {
        minioService.deleteFile(fileName);
    }

    @SneakyThrows
    public void downloadFromMinio(ImageModel image, HttpServletResponse response) {
        try (InputStream is = minioService.downloadInputStream(image.getFileName());
             OutputStream os = response.getOutputStream()) {

            response.setStatus(200);
            response.setContentType(image.getContentType());
            response.setContentLength(image.getSize().intValue());
            is.transferTo(os);
        }
    }

    public ImageModel findImageByImageIdFromImageList(UUID imageId, List<ImageModel> images) {
        for (ImageModel image : images) {
            if (image.getImageId().equals(imageId)) {
                return image;
            }
        }
        throw new DownloadImageException("image not found");
    }
}
