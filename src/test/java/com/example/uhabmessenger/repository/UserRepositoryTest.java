package com.example.uhabmessenger.repository;

import com.example.uhabmessenger.bd.init.BaseIntegrationTest;
import com.example.uhabmessenger.model.ImageModel;
import com.example.uhabmessenger.model.UserModel;
import com.example.uhabmessenger.repository.entity.ImageRepository;
import com.example.uhabmessenger.repository.entity.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageRepository imageRepository;

    @BeforeEach
    public void repositoryDel() {
        imageRepository.deleteAll();
        userRepository.deleteAll();
    }

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

    @Test
    @Transactional
    void shouldImageSaveWithUserSaving() {

        assertThat(imageRepository.findAll().size()).isEqualTo(0);

        ImageModel imageModel = ImageModel.builder()
                .contentType("ct")
                .size(12345L)
                .fileName("FName")
                .build();

        UserModel user = UserModel.builder()
                .name("name")
                .lastname("last")
                .password("pass")
                .build();
        user.getImages().add(imageModel);

        userRepository.save(user);

        assertThat(imageRepository.findAll().size()).isEqualTo(1);
    }
}
