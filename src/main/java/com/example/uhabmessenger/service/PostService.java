package com.example.uhabmessenger.service;

import com.example.uhabmessenger.dto.comment.AddCommentDto;
import com.example.uhabmessenger.dto.comment.CommentInfoDto;
import com.example.uhabmessenger.dto.posts.CreatePostDto;
import com.example.uhabmessenger.dto.posts.PostInfoDto;
import com.example.uhabmessenger.mapper.PostMapstructService;
import com.example.uhabmessenger.model.*;
import com.example.uhabmessenger.repository.entity.PostRepository;
import com.example.uhabmessenger.service.groups.SimpleGroupService;
import com.example.uhabmessenger.service.user.other.SimpleUserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostMapstructService postMapstructService;

    private final SimpleUserService simpleUserService;

    private final SimpleGroupService simpleGroupService;

    private final PostRepository postRepository;

    private final ImageService imageService;

    public void userPostSave(CreatePostDto createPostDto, List<MultipartFile> multipartFiles) {

        PostModel postModel = save(createPostDto, multipartFiles);
        userPostSaveIntoUser(createPostDto.groupOrUserId(), postModel);

    }

    private void userPostSaveIntoUser(UUID userId, PostModel beginnerPostModel) {

        UserModel userModel = simpleUserService.findById(userId);
        userModel.getPosts().add(beginnerPostModel);

        simpleUserService.save(userModel);

    }

    public void groupPostSave(CreatePostDto createPostDto, List<MultipartFile> multipartFiles) {

        PostModel postModel = save(createPostDto, multipartFiles);
        savePostIntoGroup(createPostDto.groupOrUserId(), postModel);

    }

    private void savePostIntoGroup(UUID groupId, PostModel postModel) {

        GroupModel groupModel = simpleGroupService.findById(groupId);
        groupModel.getPosts().add(postModel);
        simpleGroupService.save(groupModel);

    }

    public PostModel save(CreatePostDto createPostDto, List<MultipartFile> multipartFiles) {

        PostModel beginnerPostModel = postMapstructService.toPostModel(createPostDto);

        List<ImageModel> images = savePostImages(multipartFiles);
        beginnerPostModel.setImages(images);

        return beginnerPostModel;

    }

    private List<ImageModel> savePostImages(List<MultipartFile> multipartFiles) {

        if (Objects.isNull(multipartFiles) || multipartFiles.isEmpty()) {
            return null;
        }

        return multipartFiles.stream()
                .filter(image -> Objects.nonNull(image) && Objects.nonNull(image.getOriginalFilename()) && !image.getOriginalFilename().isBlank())
                .map(imageService::uploadImage)
                .toList();

    }

    @Transactional(readOnly = true)
    public PostInfoDto getPostInfo(UUID postId) {

        PostModel postModel = postRepository.findByPostId(postId)
                .orElseThrow(() -> new EntityNotFoundException("post not found"));

        return modelToInfoDtoWithListImageIds(postModel);

    }

    public PostInfoDto modelToInfoDtoWithListImageIds(PostModel model) {

        return addImagesIdsToDto(
                model,
                postMapstructService.toPostInfoDto(model)
        );

    }

    private PostInfoDto addImagesIdsToDto(PostModel postModel, PostInfoDto postInfoDto) {

        postInfoDto.setImagesIds(
                getImagesIdsFromPostModel(postModel)
        );
        return postInfoDto;

    }

    private List<UUID> getImagesIdsFromPostModel(PostModel postModel) {

        return postModel.getImages().stream()
                .map(ImageModel::getImageId)
                .toList();

    }

    public List<PostInfoDto> createPostInfoDtosByPostModelList(List<PostModel> posts) {

        return posts.stream()
                .map(this::modelToInfoDtoWithListImageIds)
                .toList();

    }

    @Transactional
    public void addComment(AddCommentDto addCommentDto) {

        PostModel postModel = postRepository.findByPostId(addCommentDto.postId())
                .orElseThrow(() ->
                        new EntityNotFoundException("post not found by id: " + addCommentDto.postId())
                );

        CommentModel commentModel = CommentModel.builder()
                .text(addCommentDto.text())
                .user(simpleUserService.findById(addCommentDto.userId()))
                .build();

        postModel.addComment(commentModel);
        postRepository.save(postModel);

    }

    @Transactional(readOnly = true)
    public List<CommentInfoDto> getCommentsByPostId(UUID postId) {

        PostModel postModel = postRepository.findByPostId(postId).orElseThrow(
                () -> new EntityNotFoundException("post not fount by " + postId)
        );

        return listCommentsToListInfoDto(postModel.getComments());

    }


    private List<CommentInfoDto> listCommentsToListInfoDto(List<CommentModel> comments) {

        return comments.stream()
                .map(comment -> CommentInfoDto.builder()
                        .text(comment.getText())
                        .userId(comment.getUser().getUserId())
                        .userName(comment.getUser().getName())
                        .userLastname(comment.getUser().getLastname())
                        .build())
                .toList();

    }

    public void imageDownload(UUID imageId, HttpServletResponse response) {

        imageService.downloadFromMinio(
                imageService.findByImageId(imageId),
                response
        );

    }

    public List<PostModel> getAllPostList() {

        return postRepository.findAll();

    }
}
