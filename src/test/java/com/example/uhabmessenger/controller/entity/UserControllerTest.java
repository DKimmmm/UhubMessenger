package com.example.uhabmessenger.controller.entity;

import com.example.uhabmessenger.dto.user.UserInfoDto;
import com.example.uhabmessenger.dto.user.UserUpdateInfoDto;
import com.example.uhabmessenger.service.user.other.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(
        value = UserController.class,
        properties = {
                "spring.profiles.active=test",
                "server.port=8008",
                "server.servlet.context-path=/uhab"
        }
)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    @SneakyThrows
    void addImageTest() {

        BDDMockito.doNothing().when(userService).uploadUserImage(any(MultipartFile.class), any(UUID.class));

        MockMultipartFile mockImageFile = new MockMultipartFile(
                "multipartFile",
                "image-test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "ldskfjds".getBytes()
        );

        UUID userUUID = new UUID(1L, 1L);

        ResultActions request = mockMvc.perform(
                MockMvcRequestBuilders.multipart(HttpMethod.POST, "/user/image/add/{userId}", userUUID)
                        .file(mockImageFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)

        );

        request.andDo(print())
                .andExpectAll(
                        status().isOk()
                );

        BDDMockito.then(userService).should(times(1)).uploadUserImage(any(), any());

    }

    @Test
    @SneakyThrows
    void downloadImageTest() {

        BDDMockito.doNothing().when(userService).downloadImageByImageAndUserIds(
                any(UUID.class), any(UUID.class), any(HttpServletResponse.class)
        );

        UUID userId = new UUID(1L, 1L);
        UUID imageId = new UUID(2L, 2L);

        ResultActions request = mockMvc.perform(
                MockMvcRequestBuilders.get("/user/image/download")
                        .param("userId", userId.toString())
                        .param("imageId", imageId.toString())
                        .contentType(MediaType.APPLICATION_JSON)

        );

        request.andDo(print())
                .andExpect(status().isOk());

        BDDMockito.then(userService).should(times(1)).downloadImageByImageAndUserIds(any(), any(), any());

    }

    @Test
    @SneakyThrows
    void deleteImageTest() {

        BDDMockito.doNothing().when(userService).deleteImage(any(UUID.class), any(UUID.class));

        UUID userId = new UUID(1L, 1L);
        UUID imageId = new UUID(2L, 2L);

        ResultActions request = mockMvc.perform(
                MockMvcRequestBuilders.delete("/user/image")
                        .param("userId", userId.toString())
                        .param("imageId", imageId.toString())
                        .contentType(MediaType.APPLICATION_JSON)

        );

        request.andDo(print())
                .andExpect(status().isOk());

        BDDMockito.then(userService).should(times(1)).deleteImage(any(), any());

    }

    @Test
    @SneakyThrows
    void updateInfoTest() {

        UserInfoDto userInfoDto = new UserInfoDto(
                "name", "lastname", "81234567890", "email@gmail.com", false, false, List.of(), List.of()
        );

        BDDMockito.given(userService.updateInfo(any(UserUpdateInfoDto.class))).willReturn(userInfoDto);

        UserUpdateInfoDto userUpdateInfoDto = new UserUpdateInfoDto(
                new UUID(1L, 1L), "Name", "Lastname", null, null
        );

        String dtoJsonValue = objectMapper.writeValueAsString(userUpdateInfoDto);

        ResultActions request = mockMvc.perform(
                MockMvcRequestBuilders.put("/user/info")
                        .content(dtoJsonValue)
                        .contentType(MediaType.APPLICATION_JSON)

        );

        request.andDo(print())
                .andExpect(status().isOk());

        BDDMockito.then(userService).should(times(1)).updateInfo(any());
    }

    @Test
    @SneakyThrows
    void updateInfoInValidDtoArgsTest() {

        UserInfoDto userInfoDto = new UserInfoDto(
                "name", "lastname", "81234567890", "email@gmail.com", false, false, List.of(), List.of()
        );

        BDDMockito.given(userService.updateInfo(any(UserUpdateInfoDto.class))).willReturn(userInfoDto);

        // 1 userId should be not null
        UserUpdateInfoDto userUpdateInfoDto = new UserUpdateInfoDto(
                null, "Name", "Lastname", null, null
        );

        String dtoJsonValue = objectMapper.writeValueAsString(userUpdateInfoDto);

        ResultActions request = mockMvc.perform(
                MockMvcRequestBuilders.put("/user/info")
                        .content(dtoJsonValue)
                        .contentType(MediaType.APPLICATION_JSON)

        );

        request.andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.title").value("Получены данные некорректного формата, попробуйте снова.")
                );


        // 2 name isn't Title case format
        UserUpdateInfoDto userUpdateInfoDto1 = new UserUpdateInfoDto(
                new UUID(1L, 1L), "name", null, null, null
        );

        String dtoJsonValue1 = objectMapper.writeValueAsString(userUpdateInfoDto1);

        ResultActions request1 = mockMvc.perform(
                MockMvcRequestBuilders.put("/user/info")
                        .content(dtoJsonValue1)
                        .contentType(MediaType.APPLICATION_JSON)

        );

        request.andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.title").value("Получены данные некорректного формата, попробуйте снова.")
                );

        // 3 phone incorrect
        UserUpdateInfoDto userUpdateInfoDto2 = new UserUpdateInfoDto(
                new UUID(1L, 1L), "Name", "Lastname", "123", null
        );

        String dtoJsonValue2 = objectMapper.writeValueAsString(userUpdateInfoDto2);

        ResultActions request2 = mockMvc.perform(
                MockMvcRequestBuilders.put("/user/info")
                        .content(dtoJsonValue2)
                        .contentType(MediaType.APPLICATION_JSON)

        );

        request2.andDo(print())
                .andExpect(status().isBadRequest());

        BDDMockito.then(userService).should(times(0)).updateInfo(any());
    }


}
