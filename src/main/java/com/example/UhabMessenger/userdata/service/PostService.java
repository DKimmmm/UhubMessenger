package com.example.UhabMessenger.userdata.service;

import com.example.UhabMessenger.userdata.config.MinioInitializer;
import com.example.UhabMessenger.userdata.dto.posts.PostDto;
import com.example.UhabMessenger.userdata.mapper.PostMapstructService;
import com.example.UhabMessenger.userdata.model.ImageModel;
import com.example.UhabMessenger.userdata.model.PostModel;
import com.example.UhabMessenger.userdata.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final PostMapstructService postMapstructService;
    private final ImageService imageService;
    private final MinioInitializer minioInitializer;


    public void save(PostDto postDto) {
        postRepository.save(mapperToModel(postDto));
    }

    private PostModel mapperToModel(PostDto postDto) {
        return postMapstructService.toPostModel(postDto);
    }

    public void uploadPostImage(MultipartFile multipartFile, UUID postId) {
        deleteIfAlreadyExists(postId);
        ImageModel imageModel = imageService.uploadImage(multipartFile);
        imageSaveInPostgres(postId, imageModel);
    }

    private void imageSaveInPostgres(UUID postId, ImageModel imageModel) {
        PostModel postModel = postRepository.findById(postId).get();
        List<ImageModel> images = postModel.getImages();
        images.add(imageModel);
        postRepository.save(postModel);
    }

    @SneakyThrows
    private void deleteIfAlreadyExists(UUID postId) {
        try {
            deleteFromMinio(postId);
            deleteFromPostImageTable(postId);
        } catch (Throwable e) {
            log.info("file not found or delete error");
        }
    }

    private void deleteFromPostImageTable(UUID postId) {
        postRepository.deleteAttachByPostId(postId);
        log.info("delete from post_images repository by postId: {}", postId);
    }

    private void deleteFromMinio(UUID postId) {
        List<String> fileNames = imageService.findByPostId(postId);
        for (String fileName : fileNames) {
            minioInitializer.deleteFile(fileName);
            log.info("minio delete by filename: {}", fileName);
        }
    }

}
