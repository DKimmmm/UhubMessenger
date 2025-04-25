package com.example.uhabmessenger.service.user.other;

import com.example.uhabmessenger.dto.posts.PostInfoDto;
import com.example.uhabmessenger.dto.user.UserInfoDto;
import com.example.uhabmessenger.dto.user.UserUpdateInfoDto;
import com.example.uhabmessenger.exception.AuthorizationErrorException;
import com.example.uhabmessenger.exception.DownloadImageException;
import com.example.uhabmessenger.exception.UsernameIncorrectException;
import com.example.uhabmessenger.formatutils.UsernameFormatUtil;
import com.example.uhabmessenger.mapper.UserMapstructService;
import com.example.uhabmessenger.model.GroupModel;
import com.example.uhabmessenger.model.ImageModel;
import com.example.uhabmessenger.model.UserModel;
import com.example.uhabmessenger.service.ImageService;
import com.example.uhabmessenger.service.PostService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
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

    public void deleteImage(UUID userId, UUID imageId) {

        ImageModel imageModel = imageService.findByImageId(imageId);
        UserModel userModel = simpleUserService.findById(userId);

        userModel.getImages().remove(imageModel);
        simpleUserService.save(userModel);

    }

    public void downloadImageByImageAndUserIds(UUID imageId, UUID userId, HttpServletResponse response) {

        try {

            List<ImageModel> images = simpleUserService.findById(userId).getImages();
            ImageModel image = imageService.findImageByImageIdFromImageList(imageId, images);
            imageService.downloadFromMinio(image, response);

        } catch (Exception e) {
            throw new DownloadImageException("error in download your needed image");
        }

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

        userInfoDto.setGroupsIds(
                groups.stream()
                        .map(GroupModel::getGroupId)
                        .toList());

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

        return userModel.getImages().stream()
                .map(ImageModel::getImageId)
                .toList();

    }

    public List<PostInfoDto> findPostsInfoListByUserId(UUID userId) {

        UserModel userModel = simpleUserService.findById(userId);
        return postService.createPostInfoDtosByPostModelList(userModel.getPosts());

    }

    public UserInfoDto updateInfo(UserUpdateInfoDto userUpdateInfoDto) {

        UserModel userModel = updateFieldIfNewDataNotNull(
                simpleUserService.findById(userUpdateInfoDto.userId()),
                userUpdateInfoDto);

        return getUserInfo(
                simpleUserService.save(userModel).getUserId()
        );

    }

    private UserModel updateFieldIfNewDataNotNull(UserModel userModel, UserUpdateInfoDto userUpdateInfoDto) {

        if (!Objects.isNull(userUpdateInfoDto.name()) && !userUpdateInfoDto.name().isBlank()) {
            userModel.setName(userUpdateInfoDto.name());
        }

        if (!Objects.isNull(userUpdateInfoDto.lastname()) && !userUpdateInfoDto.lastname().isBlank()) {
            userModel.setLastname(userUpdateInfoDto.lastname());
        }

        if (!Objects.isNull(userUpdateInfoDto.email()) && !userUpdateInfoDto.email().isBlank()) {
            userModel.setEmail(userUpdateInfoDto.email());
            userModel.setApprovedEmail(false);
        }

        if (!Objects.isNull(userUpdateInfoDto.phone()) && !userUpdateInfoDto.phone().isBlank()) {
            userModel.setPhone(userUpdateInfoDto.phone());
            userModel.setApprovedPhone(false);
        }

        return userModel;

    }
}