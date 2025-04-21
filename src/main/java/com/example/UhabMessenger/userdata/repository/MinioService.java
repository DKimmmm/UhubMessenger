package com.example.UhabMessenger.userdata.repository;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @SneakyThrows
    public void uploadFile(String fileName, InputStream inputStream, long contentLength) {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(inputStream, contentLength, -1)
                        .build());

        log.info("'{}' is successfully uploaded as object '{}' to bucket '{}'.",
                fileName, fileName, bucketName);
    }

    @SneakyThrows
    public InputStream downloadInputStream(String fileName) {
        InputStream inputStream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build());

        log.info("Object '{}' is successfully downloaded from bucket '{}'.",
                fileName, bucketName);

        return inputStream;
    }

    @SneakyThrows
    public void deleteFile(String fileName) {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build());

        log.info("Object '{}' is successfully deleted from bucket '{}'.",
                fileName, bucketName);
    }
}
