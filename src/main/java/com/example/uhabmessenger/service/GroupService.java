package com.example.uhabmessenger.service;

import com.example.uhabmessenger.dto.groups.GroupInfoDto;
import com.example.uhabmessenger.mapper.GroupMapstructService;
import com.example.uhabmessenger.model.GroupModel;
import com.example.uhabmessenger.model.UserModel;
import com.example.uhabmessenger.repository.entity.GroupRepository;
import com.example.uhabmessenger.service.user.other.SimpleSaveAndFindUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final SimpleSaveAndFindUserService simpleUserService;
    private final GroupMapstructService groupMapstructService;

    public void save(String title, String description, List<UUID> userIds) {
        GroupModel groupBuild = GroupModel.builder().title(title).description(description).build();
        List<UserModel> users = findUsersByUserIds(userIds);
        groupRepository.save(addUsersIntoGroup(groupBuild, users));
    }

    private List<UserModel> findUsersByUserIds(List<UUID> userIds) {
        if (userIds == null || userIds.isEmpty()) {
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
}
