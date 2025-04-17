package com.example.UhabMessenger.userdata.service;

import com.example.UhabMessenger.userdata.config.MinioInitializer;
import com.example.UhabMessenger.userdata.dto.posts.PostDto;
import com.example.UhabMessenger.userdata.dto.posts.PostInfoDto;
import com.example.UhabMessenger.userdata.mapper.PostMapstructService;
import com.example.UhabMessenger.userdata.model.ImageModel;
import com.example.UhabMessenger.userdata.model.PostModel;
import com.example.UhabMessenger.userdata.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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


    public UUID save(String title, String description, List<MultipartFile> multipartFiles) {
        checkForOneNotNullField(title, multipartFiles);
        PostModel beginnerPostModel = mapperToModel(title, description);
        List<ImageModel> images = savePostImages(multipartFiles);
        beginnerPostModel.setImages(images);
        return postRepository.save(beginnerPostModel).getPostId();
    }

    private void checkForOneNotNullField(String title, List<MultipartFile> multipartFiles) {
        log.info("add custom exception");
        if ((title == null || title.isBlank()) && (multipartFiles == null || multipartFiles.isEmpty())) {
            throw new RuntimeException();
        }
    }

    private List<ImageModel> savePostImages(List<MultipartFile> multipartFiles) {
        List<ImageModel> result = new ArrayList<>();
        if (multipartFiles == null || multipartFiles.isEmpty()) {
            return null;
        }
        for (MultipartFile multipartFile : multipartFiles) {
            result.add(imageService.uploadImage(multipartFile));
        }
        return result;
    }

    private PostModel mapperToModel(String title, String description) {
        return postMapstructService.toPostModel(new PostDto(title, description));
    }

//    public ImageModel uploadPostImage(MultipartFile multipartFile) {
////        deleteIfAlreadyExists(postId);
//        return

    /// /        imageSaveInPostgres(postId, imageModel);
//    }
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

    public PostInfoDto getPostInfo(UUID postId) {
        PostModel postModel = postRepository.findByPostId(postId).orElse(null);
        log.info("add customer exception");
        if (postModel == null) {
            throw new RuntimeException();
        }
        PostInfoDto postInfoDto = postMapstructService.toPostInfoDto(postModel);
        return addImagesIdsToDto(postModel, postInfoDto);
    }

    private PostInfoDto addImagesIdsToDto(PostModel postModel, PostInfoDto postInfoDto) {
        postInfoDto.setImagesIds(getImagesIdsFromPostModel(postModel));
        return postInfoDto;
    }

    private List<UUID> getImagesIdsFromPostModel(PostModel postModel) {
        List<UUID> result = new ArrayList<>();
        for (ImageModel image : postModel.getImages()) {
            result.add(image.getImageId());
        }
        return result;
    }
}
