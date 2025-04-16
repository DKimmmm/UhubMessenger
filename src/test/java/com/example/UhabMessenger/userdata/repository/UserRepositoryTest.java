package com.example.UhabMessenger.userdata.repository;

import com.example.UhabMessenger.bd.init.BaseIntegrationTest;
import com.example.UhabMessenger.userdata.model.UserModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndFindUser() {
        UserModel user = UserModel.builder()
                .name("testUser")
                .lastname("testLastname")
                .password("pass1234")
                .email("test@example.com").build();
        userRepository.save(user);

        UserModel found = userRepository.findByEmail("test@example.com").orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("testUser");
    }
}
