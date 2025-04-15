package com.example.UhabMessenger.userdata.config;

import io.minio.*;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Slf4j
@Component
public class MinioInitializer {

    private MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Value("${minio.url}")
    private String minioUrl;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @SneakyThrows
    @PostConstruct
    public void init() {
        minioClient =
                MinioClient.builder()
                        .endpoint(minioUrl)
                        .credentials(accessKey, secretKey)
                        .build();

        boolean found =
                minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    @SneakyThrows
    public void uploadFile(String fileName, InputStream inputStream, long contentLength) {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(inputStream, contentLength, -1) // -1 для автоматического определения размера части
                        .build());

        log.info(
                "'{}' is successfully uploaded as object '{}' to bucket '{}'.",
                fileName, fileName, bucketName);
    }

    @SneakyThrows
    public InputStream downloadInputStream(String fileName){
        InputStream inputStream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build());

        log.info(
                "Object '{}' is successfully downloaded from bucket '{}'.",
                fileName, bucketName);

        return inputStream;
    }

    @SneakyThrows
    public void deleteFile(String fileName){
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build());

        log.info(
                "Object '{}' is successfully deleted from bucket '{}'.",
                fileName, bucketName);
    }

}
