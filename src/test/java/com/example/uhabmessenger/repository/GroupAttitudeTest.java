package com.example.uhabmessenger.repository;

import com.example.uhabmessenger.bd.init.BaseIntegrationTest;
import com.example.uhabmessenger.model.GroupModel;
import com.example.uhabmessenger.model.UserModel;
import com.example.uhabmessenger.repository.entity.GroupRepository;
import com.example.uhabmessenger.repository.entity.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GroupAttitudeTest extends BaseIntegrationTest {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    void saveAndFindByIdTest() {
        UUID uuid = saveTwoGroups();
        assertThat(groupRepository.count()).isEqualTo(2);
        assertThat(groupRepository.findByGroupId(uuid).orElse(null)).isNotNull();
    }

    private UUID saveTwoGroups() {
        GroupModel.GroupModelBuilder groupBuilder = GroupModel.builder();
        groupBuilder.title("title").description("des");
        groupRepository.save(groupBuilder.build());

        GroupModel.GroupModelBuilder groupBuilder1 = GroupModel.builder();
        groupBuilder1.title("tt").description("des");
        return groupRepository.save(groupBuilder1.build()).getGroupId();
    }

    @Test
    @Transactional
    void saveCascadeUserFromGroup() {
        UserModel userModel = createUser();
        GroupModel groupModel = createGroupByUser(userModel);

        UserModel userWithGroup = userRepository.findByUserId(userModel.getUserId()).orElse(null);
        assertThat(userWithGroup).isNotNull();

        Assertions.assertThat(userWithGroup).isNotNull();
        assertThat(userWithGroup.getGroups().isEmpty()).isEqualTo(false);
        assertThat(userWithGroup.getGroups().getFirst().getGroupId()).isEqualTo(groupModel.getGroupId());
    }

    @BeforeEach
    public void deleteData() {
        groupRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    void deleteCascadeUserFromBd() {

        List<UserModel> users = createUserList();
        UserModel firstUser = users.getFirst();
        UserModel secondUser = users.get(1);

        GroupModel group1 = createGroupByUser(firstUser);

        GroupModel build = GroupModel.builder().title("titile").description("ddddees").build();
        build.addUser(firstUser);
        build.addUser(secondUser);

        GroupModel group2 = groupRepository.save(build);

        Assertions.assertThat(group1.getUsers().size()).isEqualTo(1);
        Assertions.assertThat(group2.getUsers().size()).isEqualTo(2);
        Assertions.assertThat(userRepository.count()).isEqualTo(2);

        group2.removeUser(secondUser);
        groupRepository.save(group2);

        Assertions.assertThat(firstUser.getGroups().size()).isEqualTo(2);
        Assertions.assertThat(secondUser.getGroups().size()).isEqualTo(0);
        Assertions.assertThat(group2.getUsers().size()).isEqualTo(1);

    }

    private GroupModel createGroupByUser(UserModel userModel) {
        GroupModel groupBuild = GroupModel.builder()
                .title("title")
                .description("des")
                .build();
        groupBuild.addUser(userModel); // Используем метод addUser для синхронизации
        return groupRepository.save(groupBuild);
    }

    private UserModel createUser() {
        return UserModel.builder()
                .name("Name")
                .lastname("LName")
                .password("pass")
                .build();
    }

    private List<UserModel> createUserList() {
        List<UserModel> result = new ArrayList<>();
        result.add(createUser());
        result.add(UserModel.builder()
                .name("Name1")
                .lastname("LName1")
                .password("pass1").build());
        return result;
    }

}
