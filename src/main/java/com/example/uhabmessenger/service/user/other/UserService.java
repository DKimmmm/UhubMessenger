package com.example.uhabmessenger.service.user.other;

import com.example.uhabmessenger.dto.posts.PostInfoDto;
import com.example.uhabmessenger.dto.user.UserInfoDto;
import com.example.uhabmessenger.exception.AuthorizationErrorException;
import com.example.uhabmessenger.exception.UncorrectedPasswordException;
import com.example.uhabmessenger.exception.UserNotFoundException;
import com.example.uhabmessenger.exception.UsernameIncorrectException;
import com.example.uhabmessenger.formatutils.UsernameFormatUtil;
import com.example.uhabmessenger.mapper.UserMapstructService;
import com.example.uhabmessenger.model.GroupModel;
import com.example.uhabmessenger.model.ImageModel;
import com.example.uhabmessenger.model.PostModel;
import com.example.uhabmessenger.model.UserModel;
import com.example.uhabmessenger.repository.MinioService;
import com.example.uhabmessenger.repository.entity.UserRepository;
import com.example.uhabmessenger.service.ImageService;
import com.example.uhabmessenger.service.PostService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final MinioService minioService;
    private final UserMapstructService userMapstructService;
    private final PostService postService;

    public UserModel getUserByUsername(String username) {
        if (UsernameFormatUtil.usernameIsEmailFormat(username)) {
            return findUserByEmail(username);
        } else if (UsernameFormatUtil.usernameIsPhoneFormat(username)) {
            return findUserByPhone(username);
        } else {
            throw new AuthorizationErrorException("username is not correct format");
        }
    }

    private UserModel findUserByEmail(String username) {

            return userRepository.findByEmail(username).orElseThrow(
                            () -> new UsernameIncorrectException("uncorrected email")
                    );

    }

    private UserModel findUserByPhone(String username) {

            return userRepository.findByPhone(username).orElseThrow(
                    () -> new UsernameIncorrectException("uncorrected phone")
            );

    }



    public void uploadUserImage(MultipartFile multipartFile, UUID userId) {
        ImageModel imageModel = imageService.uploadImage(multipartFile);
        addImageIntoUserImageList(userId, imageModel);
    }

    private void addImageIntoUserImageList(UUID userId, ImageModel imageModel) {
        UserModel userModel = userRepository.findById(userId).get();
        userModel.getImages().add(imageModel);
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
            minioService.deleteFile(fileName);
            log.info("minio delete by filename: {}", fileName);
        }
    }


    public void deleteImage(UUID userId, UUID imageId) {
        ImageModel imageModel = imageService.findByImageId(imageId);
        UserModel userModel = userRepository.findById(userId).get();
        userModel.getImages().remove(imageModel);
        userRepository.save(userModel);
    }

    public void downloadImageByImageAndUserIds(UUID imageId, UUID userId, HttpServletResponse response) {
        List<ImageModel> images = userRepository.findByUserId(userId).get().getImages();
        if (images == null || images.isEmpty()) {
            return;
        }
        ImageModel image = findImageByImageIdFromImageList(imageId, images);
        if (image == null) {
            return;
        }
        downloadImage(image, response);
    }

    public void downloadImage(ImageModel image, HttpServletResponse response) {
        try {
            try (InputStream is = minioService.downloadInputStream(image.getFileName());
                 OutputStream os = response.getOutputStream()) {

                response.setStatus(200);
                response.setContentType(image.getContentType());
                response.setContentLength(image.getSize().intValue());
                is.transferTo(os);
            }
        } catch (Exception e) {
            log.warn("error in download user image");
        }
    }

    private ImageModel findImageByImageIdFromImageList(UUID imageId, List<ImageModel> images) {
        for (ImageModel image : images) {
            if (image.getImageId().equals(imageId)) {
                return image;
            }
        }
        return null;
    }

    public UserInfoDto getUserInfo(UUID userId) {
        UserModel userModel = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("not found by id"));

        UserInfoDto userInfoDto = userMapstructService.toUserInfoDto(userModel);

        return addGroupIdsIntoDto(
                addImagesIdsToDto(userModel, userInfoDto),
                userModel.getGroups()
        );
    }

    private UserInfoDto addGroupIdsIntoDto(UserInfoDto userInfoDto, List<GroupModel> groups) {
        List<UUID> groupIds = new ArrayList<>();
        for (GroupModel group : groups) {
            groupIds.add(group.getGroupId());
        }
        userInfoDto.setGroupsIds(groupIds);
        return userInfoDto;
    }

    private UserInfoDto addImagesIdsToDto(UserModel userModel, UserInfoDto userInfoDto) {
        if (userInfoDto != null) {
            userInfoDto.setImagesIds(collectImagesIdsFromUserModel(userModel));
            return userInfoDto;
        }
        return null;
    }

    private List<UUID> collectImagesIdsFromUserModel(UserModel userModel) {
        List<UUID> result = new ArrayList<>();
        for (ImageModel image : userModel.getImages()) {
            result.add(image.getImageId());
        }
        return result;
    }

    public List<PostInfoDto> findPostsInfoListByUserId(UUID userId) {
        UserModel userModel = userRepository.findByUserId(userId).orElseThrow();
        return createPostInfoDtosByPostModelList(userModel.getPosts());
    }

    private List<PostInfoDto> createPostInfoDtosByPostModelList(List<PostModel> posts) {
        List<PostInfoDto> result = new ArrayList<>();
        for (PostModel post : posts) {
            result.add(postService.modelToInfoDtoWithListImageIds(post));
        }
        return result;
    }
}
