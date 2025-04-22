package com.example.uhabmessenger.service;

import com.example.uhabmessenger.dto.groups.GroupInfoDto;
import com.example.uhabmessenger.exception.GroupNotFoundException;
import com.example.uhabmessenger.exception.GroupSaveException;
import com.example.uhabmessenger.exception.UserNotFoundException;
import com.example.uhabmessenger.mapper.GroupMapstructService;
import com.example.uhabmessenger.model.GroupModel;
import com.example.uhabmessenger.model.UserModel;
import com.example.uhabmessenger.repository.entity.GroupRepository;
import com.example.uhabmessenger.service.user.other.SimpleUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final SimpleUserService simpleUserService;
    private final GroupMapstructService groupMapstructService;

    public void save(String title, String description, List<UUID> userIds) {
        checkExistByTitle(title);
        try {
            GroupModel groupBuild = GroupModel.builder().title(title).description(description).build();
            List<UserModel> users = findUsersByUserIds(userIds);
            groupRepository.save(addUsersIntoGroup(groupBuild, users));
        } catch (Exception e) {
            throw new GroupSaveException("Group save error" + e.getMessage());
        }
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
        return addUserIdsIntoInfoDtos(result, group.getUsers());
    }

    private GroupInfoDto addUserIdsIntoInfoDtos(GroupInfoDto result, List<UserModel> users) {
        List<UUID> userIds = new ArrayList<>();
        for (UserModel user : users) {
            userIds.add(user.getUserId());
        }
        result.setUserIds(userIds);
        return result;
    }

    public GroupInfoDto getInfo(UUID groupId) {
        GroupModel groupModel = groupRepository.findByGroupId(groupId)
                .orElseThrow(() -> new GroupNotFoundException("group not found by " + groupId));

        return groupToInfoDto(groupModel);

    }

    public void addMembers(UUID groupId, List<UUID> membersIds) {

        GroupModel groupModel = groupRepository.findByGroupId(groupId)
                .orElseThrow(() -> new GroupNotFoundException("group not found by " + groupId));

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
}
