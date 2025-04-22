package com.example.uhabmessenger.service.user.other;

import com.example.uhabmessenger.dto.posts.PostInfoDto;
import com.example.uhabmessenger.dto.user.UserInfoDto;
import com.example.uhabmessenger.exception.AuthorizationErrorException;
import com.example.uhabmessenger.exception.DownloadImageException;
import com.example.uhabmessenger.exception.UsernameIncorrectException;
import com.example.uhabmessenger.formatutils.UsernameFormatUtil;
import com.example.uhabmessenger.mapper.UserMapstructService;
import com.example.uhabmessenger.model.GroupModel;
import com.example.uhabmessenger.model.ImageModel;
import com.example.uhabmessenger.model.PostModel;
import com.example.uhabmessenger.model.UserModel;
import com.example.uhabmessenger.service.ImageService;
import com.example.uhabmessenger.service.PostService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final ImageService imageService;
    private final UserMapstructService userMapstructService;
    private final PostService postService;
    private final SimpleUserService simpleUserService;

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

        try {
            return simpleUserService.findByEmail(username);
        } catch (Exception e) {
            throw new UsernameIncorrectException("uncorrected email");
        }

    }

    private UserModel findUserByPhone(String username) {

        try {
            return simpleUserService.findByPhone(username);
        } catch (Exception e) {
            throw new UsernameIncorrectException("uncorrected phone");
        }

    }

    public void uploadUserImage(MultipartFile multipartFile, UUID userId) {

        ImageModel imageModel = imageService.uploadImage(multipartFile);
        addImageIntoUserImageList(userId, imageModel);

    }

    private void addImageIntoUserImageList(UUID userId, ImageModel imageModel) {

        UserModel userModel = simpleUserService.findById(userId);
        userModel.getImages().add(imageModel);
        simpleUserService.save(userModel);

    }

    private void deleteFromMinio(UUID userId) {

        List<String> fileNames = imageService.findByUserId(userId);
        for (String fileName : fileNames) {
            imageService.deleteFromMinio(fileName);
        }

    }

    public void deleteImage(UUID userId, UUID imageId) {

        ImageModel imageModel = imageService.findByImageId(imageId);
        UserModel userModel = simpleUserService.findById(userId);
        userModel.getImages().remove(imageModel);
        simpleUserService.save(userModel);

    }

    public void downloadImageByImageAndUserIds(UUID imageId, UUID userId, HttpServletResponse response) {
        try {
            List<ImageModel> images = simpleUserService.findById(userId).getImages();
            ImageModel image = findImageByImageIdFromImageList(imageId, images);
            downloadImage(image, response);
        } catch (Exception e) {
            throw new DownloadImageException("error in download your needed image");
        }
    }

    public void downloadImage(ImageModel image, HttpServletResponse response) {

        try {
            imageService.downloadFromMinio(image, response);
        } catch (Exception e) {
            throw new DownloadImageException("error in download your needed image");
        }

    }

    private ImageModel findImageByImageIdFromImageList(UUID imageId, List<ImageModel> images) {

        for (ImageModel image : images) {
            if (image.getImageId().equals(imageId)) {
                return image;
            }
        }
        throw new DownloadImageException("image not found");

    }

    public UserInfoDto getUserInfo(UUID userId) {
        UserModel userModel = simpleUserService.findById(userId);

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

        UserModel userModel = simpleUserService.findById(userId);
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
