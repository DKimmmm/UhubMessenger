//package com.example.UhabMessenger.user_data.controller.service;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.io.InputStream;
//
//@Service
//public class MinioService {
//
//    private final S3Client s3Client;
//
//    @Value("${minio.bucket-name}")
//    private String bucketName;
//
//    public MinioService(S3Client s3Client) {
//        this.s3Client = s3Client;
//    }
//
//    public void uploadFile(String fileName, InputStream inputStream, long contentLength) {
//        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
//                .bucket(bucketName)
//                .key(fileName)
//                .build();
//
//        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, contentLength));
//    }
//}
