package com.example.uhabmessenger.controller.auth;

import com.example.uhabmessenger.dto.register.LoginDto;
import com.example.uhabmessenger.dto.register.SignUpDto;
import com.example.uhabmessenger.service.user.authorization.AuthUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        value = AuthorizationController.class,
        properties = {
                "spring.profiles.active=test",
                "server.port=8080",
                "server.servlet.context-path=/uhab"
        },
        excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthUserService authUserService;

    @Test
    @DisplayName(value = "test for good start")
    public void test() {
    }

    @Test
    @SneakyThrows
    void signupTest() {

        // 1. with email

        SignUpDto signUpDto = new SignUpDto(
                "Name",
                "Lastname",
                "email@gmail.com",
                "passwordN1"
        );
        doNothing().when(authUserService).signup(signUpDto);

        String requestJson = objectMapper.writeValueAsString(signUpDto);

        ResultActions request = mockMvc.perform(post("/authorization/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        );

        request.andDo(print())
                .andExpect(status().isOk());

        // 2. with phone

        SignUpDto signUpDto1 = new SignUpDto(
                "Name",
                "Lastname",
                "81234567890",
                "passwordN1"
        );
        doNothing().when(authUserService).signup(signUpDto1);

        String requestJson1 = objectMapper.writeValueAsString(signUpDto1);

        ResultActions request1 = mockMvc.perform(post("/authorization/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson1)
        );

        request1.andDo(print())
                .andExpect(status().isOk());

        BDDMockito.then(authUserService).should(times(2)).signup(any());

    }

    @Test
    @SneakyThrows
    void signupBadRequestInvalidNameAndLastnameTest() {

        // 1. name isn't Title case

        SignUpDto signUpDto = new SignUpDto(
                "name",
                "Lastname",
                "email@gmail.com",
                "passwordN1"
        );
        doNothing().when(authUserService).signup(signUpDto);

        String requestJson = objectMapper.writeValueAsString(signUpDto);

        ResultActions request = mockMvc.perform(post("/authorization/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        );

        request.andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.title", Matchers.is("Получены данные некорректного формата, попробуйте снова."))
                );

        // 2 lastname isn't title case

        SignUpDto signUpDto1 = new SignUpDto(
                "Name",
                "lastname",
                "email@gmail.com",
                "passwordN1"
        );
        doNothing().when(authUserService).signup(signUpDto1);

        String requestJson1 = objectMapper.writeValueAsString(signUpDto1);

        ResultActions request1 = mockMvc.perform(post("/authorization/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson1)
        );

        request1.andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.title", Matchers.is("Получены данные некорректного формата, попробуйте снова."))
                );

        BDDMockito.then(authUserService).should(times(0)).signup(any());

    }

    @Test
    @SneakyThrows
    void signupBadRequestInvalidPhotoAndEmailFormatTest() {

        // 1. email isn't validated

        SignUpDto signUpDto = new SignUpDto(
                "Name",
                "Lastname",
                "email.gmail.com",
                "passwordN1"
        );
        doNothing().when(authUserService).signup(signUpDto);

        String requestJson = objectMapper.writeValueAsString(signUpDto);

        ResultActions request = mockMvc.perform(post("/authorization/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        );

        request.andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.title", Matchers.is("Получены данные некорректного формата, попробуйте снова."))
                );

        // 2 phone isn't valid

        SignUpDto signUpDto1 = new SignUpDto(
                "Name",
                "Lastname",
                "85890",
                "passwordN1"
        );
        doNothing().when(authUserService).signup(signUpDto1);

        String requestJson1 = objectMapper.writeValueAsString(signUpDto1);

        ResultActions request1 = mockMvc.perform(post("/authorization/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson1)
        );

        request1.andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.title", Matchers.is("Получены данные некорректного формата, попробуйте снова."))
                );

        BDDMockito.then(authUserService).should(times(0)).signup(any());

    }

    @Test
    @SneakyThrows
    void signupBadRequestPasswordIncorrectTest() {

        // without Upper case

        SignUpDto signUpDto = new SignUpDto(
                "Name",
                "Lastname",
                "email@gmail.com",
                "password1"
        );
        doNothing().when(authUserService).signup(signUpDto);

        String requestJson = objectMapper.writeValueAsString(signUpDto);

        ResultActions request = mockMvc.perform(post("/authorization/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        );

        request.andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.title", Matchers.is("Получены данные некорректного формата, попробуйте снова."))
                );

        // 2 without number

        SignUpDto signUpDto1 = new SignUpDto(
                "Name",
                "Lastname",
                "email@gmail.com",
                "passwordN"
        );
        doNothing().when(authUserService).signup(signUpDto1);

        String requestJson1 = objectMapper.writeValueAsString(signUpDto1);

        ResultActions request1 = mockMvc.perform(post("/authorization/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson1)
        );

        request1.andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.title", Matchers.is("Получены данные некорректного формата, попробуйте снова."))
                );

        BDDMockito.then(authUserService).should(times(0)).signup(any());

    }

    @Test
    @SneakyThrows
    void loginTest() {

        // 1. with email

        LoginDto loginDto = new LoginDto(
                "email@gmail.com",
                "passwordN1"
        );

        UUID userId = new UUID(17643L, 134674L);

        BDDMockito.given(authUserService.login(loginDto)).willReturn(userId);

        String requestJson = objectMapper.writeValueAsString(loginDto);

        ResultActions request = mockMvc.perform(post("/authorization/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        );

        request.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$", Matchers.is("00000000-0000-44eb-0000-000000020e12"))
                );

        // 2. with phone

        LoginDto loginDto1 = new LoginDto(
                "81234567890",
                "passwordN1"
        );

        UUID userId1 = new UUID(13562L, 2452352L);

        BDDMockito.given(authUserService.login(loginDto1)).willReturn(userId1);

        String requestJson1 = objectMapper.writeValueAsString(loginDto1);

        ResultActions request1 = mockMvc.perform(post("/authorization/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson1)
        );

        request1.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$", Matchers.is("00000000-0000-34fa-0000-000000256b80"))
                );

        BDDMockito.then(authUserService).should(times(2)).login(any());

    }

    @Test
    @SneakyThrows
    void loginBadRequestInvalidUsernameFormatTest() {

        // 1. email isn't validated

        LoginDto loginDto = new LoginDto(
                "email.gmail.com",
                "passwordN1"
        );

        UUID userId = new UUID(17643L, 134674L);

        BDDMockito.given(authUserService.login(loginDto)).willReturn(userId);

        String requestJson = objectMapper.writeValueAsString(loginDto);

        ResultActions request = mockMvc.perform(post("/authorization/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        );

        request.andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.title", Matchers.is("Получены данные некорректного формата, попробуйте снова."))
                );

        // 2 phone isn't valid

        LoginDto loginDto1 = new LoginDto(
                "85890",
                "passwordN1"
        );

        UUID userId1 = new UUID(13562L, 2452352L);

        BDDMockito.given(authUserService.login(loginDto1)).willReturn(userId1);

        String requestJson1 = objectMapper.writeValueAsString(loginDto1);

        ResultActions request1 = mockMvc.perform(post("/authorization/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson1)
        );

        request1.andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.title", Matchers.is("Получены данные некорректного формата, попробуйте снова."))
                );

        BDDMockito.then(authUserService).should(times(0)).signup(any());

    }

    @Test
    @SneakyThrows
    void loginBadRequestPasswordIncorrectTest() {

        // without Upper case

        LoginDto loginDto = new LoginDto(
                "email@gmail.com",
                "password1"
        );

        UUID userId = new UUID(17643L, 134674L);

        BDDMockito.given(authUserService.login(loginDto)).willReturn(userId);

        String requestJson = objectMapper.writeValueAsString(loginDto);

        ResultActions request = mockMvc.perform(post("/authorization/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        );

        request.andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.title", Matchers.is("Получены данные некорректного формата, попробуйте снова."))
                );

        // 2 without number

        LoginDto loginDto1 = new LoginDto(
                "email@gmail.com",
                "passwordN"
        );

        UUID userId1 = new UUID(13562L, 2452352L);

        BDDMockito.given(authUserService.login(loginDto1)).willReturn(userId1);

        String requestJson1 = objectMapper.writeValueAsString(loginDto1);

        ResultActions request1 = mockMvc.perform(post("/authorization/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson1)
        );

        request1.andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.title", Matchers.is("Получены данные некорректного формата, попробуйте снова."))
                );

        BDDMockito.then(authUserService).should(times(0)).signup(any());

    }

}
