package com.example.uhabmessenger.controller.auth;

import com.example.uhabmessenger.dto.register.SignUpDto;
import com.example.uhabmessenger.service.user.authorization.AuthUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
        SignUpDto signUpDto = new SignUpDto(
                "Name",
                "Lastname",
                "email@gmail.com",
                "passwordN1"
        );
        doNothing().when(authUserService).signup(signUpDto);


        String requestJson = objectMapper.writeValueAsString(signUpDto);

        mockMvc.perform(post("/authorization/signup")

                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)).andDo(print())
                .andExpect(status().isOk());

        then(authUserService).should().signup(signUpDto);

    }

}
