package com.example.UhabMessenger.userdata.repository;

import com.example.UhabMessenger.bd.init.BaseIntegrationTest;
import com.example.UhabMessenger.userdata.model.GroupModel;
import com.example.UhabMessenger.userdata.model.UserModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
        groupRepository.deleteAll();
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

        boolean isEmpty = userWithGroup.getGroups().isEmpty();
        assertThat(isEmpty).isEqualTo(false);
        assertThat(userWithGroup.getGroups().getFirst().getGroupId()).isEqualTo(groupModel.getGroupId());
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

}
