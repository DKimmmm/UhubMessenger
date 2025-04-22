package com.example.uhabmessenger.service;

import com.example.uhabmessenger.dto.groups.GroupCreateDto;
import com.example.uhabmessenger.dto.groups.GroupInfoDto;
import com.example.uhabmessenger.exception.DownloadImageException;
import com.example.uhabmessenger.exception.GroupNotFoundException;
import com.example.uhabmessenger.exception.GroupSaveException;
import com.example.uhabmessenger.exception.ImageSaveException;
import com.example.uhabmessenger.mapper.GroupMapstructService;
import com.example.uhabmessenger.model.GroupModel;
import com.example.uhabmessenger.model.ImageModel;
import com.example.uhabmessenger.model.UserModel;
import com.example.uhabmessenger.repository.entity.GroupRepository;
import com.example.uhabmessenger.service.user.other.SimpleUserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final SimpleUserService simpleUserService;
    private final GroupMapstructService groupMapstructService;
    private final ImageService imageService;

    public void save(GroupCreateDto groupCreateDto) {
        checkExistByTitle(groupCreateDto.title());
        try {
            GroupModel groupBuild = groupMapstructService.toModel(groupCreateDto);
            List<UUID> userIds = deleteDuplicate(groupCreateDto.userIds());
            List<UserModel> users = findUsersByUserIds(userIds);
            groupRepository.save(addUsersIntoGroup(groupBuild, users));
        } catch (Exception e) {
            throw new GroupSaveException("Group save error" + e.getMessage());
        }
    }

    private List<UUID> deleteDuplicate(List<UUID> userIds) {
        return userIds.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    private void removeAlreadyExists(List<UUID> usersIdForAdded, List<UserModel> userAlreadyThere) {
        List<UUID> userAlreadyIds = findUserIdsByUserModels(userAlreadyThere);
        usersIdForAdded.removeAll(userAlreadyIds);
    }

    private List<UUID> findUserIdsByUserModels(List<UserModel> users1) {
        List<UUID> result = new ArrayList<>();
        for (UserModel userModel : users1) {
            result.add(userModel.getUserId());
        }
        return result;
    }

    private void checkExistByTitle(String title) {
        if (groupRepository.existsByTitle(title)) {
            throw new GroupSaveException("Group with title '" + title + "' already exists");
        }
    }

    private List<UserModel> findUsersByUserIds(List<UUID> userIds) {
        if (Objects.isNull(userIds) || userIds.isEmpty()) {
            return new ArrayList<>();
        }
        List<UserModel> result = new ArrayList<>();
        for (UUID userId : userIds) {
            result.add(simpleUserService.findById(userId));
        }
        return result;
    }

    private GroupModel addUsersIntoGroup(GroupModel groupBuild, List<UserModel> users) {
        if (users.isEmpty()) {
            return groupBuild;
        }
        for (UserModel user : users) {
            groupBuild.addUser(user);
        }
        return groupBuild;
    }

    public List<GroupInfoDto> getAllInfo() {

        List<GroupModel> allGroups = groupRepository.findAll();

        List<GroupInfoDto> result = new ArrayList<>();

        for (GroupModel group : allGroups) {
            result.add(groupToInfoDto(group));
        }

        return result;
    }

    private GroupInfoDto groupToInfoDto(GroupModel group) {
        GroupInfoDto result = groupMapstructService.toInfoDto(group);
        addUserIdsIntoInfoDto(result, group.getUsers());
        addImageIdsToGroupInfoDto(result, group.getImages());
        return result;
    }

    private void addImageIdsToGroupInfoDto(GroupInfoDto result, List<ImageModel> images) {
        List<UUID> imagesIds = new ArrayList<>();
        for (ImageModel image : images) {
            imagesIds.add(image.getImageId());
        }
        result.setPhotoIds(imagesIds);
    }

    private void addUserIdsIntoInfoDto(GroupInfoDto result, List<UserModel> users) {
        List<UUID> userIds = new ArrayList<>();
        for (UserModel user : users) {
            userIds.add(user.getUserId());
        }
        result.setUserIds(userIds);
    }

    public GroupInfoDto getInfo(UUID groupId) {
        GroupModel groupModel = groupRepository.findByGroupId(groupId)
                .orElseThrow(() -> new GroupNotFoundException("group not found by " + groupId));

        return groupToInfoDto(groupModel);

    }

    public void addMembers(UUID groupId, List<UUID> membersIds) {

        GroupModel groupModel = groupRepository.findByGroupId(groupId)
                .orElseThrow(() -> new GroupNotFoundException("group not found by " + groupId));
        membersIds = deleteDuplicate(membersIds);
        removeAlreadyExists(membersIds, groupModel.getUsers());
        List<UserModel> usersByUserIds = findUsersByUserIds(membersIds);

        groupRepository.save(addUsersIntoGroup(groupModel, usersByUserIds));

    }

    public void removeMember(UUID groupId, UUID memberId) {
        GroupModel groupModel = groupRepository.findByGroupId(groupId)
                .orElseThrow(() -> new GroupNotFoundException("group not found by " + groupId));

        groupModel.removeUser(simpleUserService.findById(memberId));

        groupRepository.save(groupModel);
    }

    public void removeById(UUID groupId) {
        groupRepository.deleteById(groupId);
    }

    public void photoUpdate(UUID groupId, MultipartFile multipartFile) {
        try {
            GroupModel groupModel = groupRepository.findByGroupId(groupId).get();
            removeOldPhoto(groupModel);
            addNewPhoto(groupModel, multipartFile);
        } catch (Exception e) {
            throw new ImageSaveException("error in photo save");
        }
    }

    private void addNewPhoto(GroupModel groupModel, MultipartFile multipartFile) {
        ImageModel imageModel = imageService.uploadImage(multipartFile);
        groupModel.getImages().add(imageModel);
        groupRepository.save(groupModel);
    }

    private void removeOldPhoto(GroupModel groupModel) {
        if (!groupModel.getImages().isEmpty()) {
            ImageModel remove = groupModel.getImages().removeFirst();
            imageService.deleteFromMinio(remove.getFileName());
        }
    }

    public void downloadImage(UUID imageId, UUID groupId, HttpServletResponse response) {
        try {
            List<ImageModel> images = groupRepository.findByGroupId(groupId)
                    .orElseThrow(()-> new GroupNotFoundException("group not found")).getImages();
            ImageModel image = imageService.findImageByImageIdFromImageList(imageId, images);
            imageService.downloadFromMinio(image, response);
        } catch (Exception e) {
            throw new DownloadImageException("error in download your needed image");
        }
    }

}
