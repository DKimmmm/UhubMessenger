package com.example.UhabMessenger.user_data.controller.config;

import io.minio.*;
import io.minio.errors.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Component
//@RequiredArgsConstructor
public class MinioInitializer {

    //    private final S3Client s3Client;
    private MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Value("${minio.url}")
    private String minioUrl;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;
//
//    public MinioInitializer(S3Client s3Client) {
//        this.s3Client = s3Client;
//    }

//    @PostConstruct
//    public void init() {
//        log.info("bucket name is {}", bucketName);
//
//        try {
//            // Проверяем, существует ли бакет
//            s3Client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
//        } catch (Exception e) {
//            // Если бакет не существует, создаем его
//            s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
//        }
//    }

    @PostConstruct
    public void init() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioClient =
                MinioClient.builder()
                        .endpoint(minioUrl)
                        .credentials(accessKey, secretKey)
                        .build();

        // Make 'asiatrip' bucket if not exist.
        boolean found =
                minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            // Make a new bucket called 'asiatrip'.
            log.info("------------------ bucket {}, !found build", bucketName);
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        } else {
            log.info("------------------ Bucket {} already exists.", bucketName);
        }
    }

    public void uploadFile(String fileName, InputStream inputStream, long contentLength) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
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

}
