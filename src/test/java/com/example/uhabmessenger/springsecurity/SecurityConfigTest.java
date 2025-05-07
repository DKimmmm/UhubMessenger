package com.example.uhabmessenger.springsecurity;

import com.example.uhabmessenger.bd.init.BaseIntegrationTest;
import com.example.uhabmessenger.dto.register.SignUpDto;
import com.example.uhabmessenger.dto.user.UserInfoDto;
import com.example.uhabmessenger.model.UserModel;
import com.example.uhabmessenger.service.user.other.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        properties = {
                "spring.profiles.active=test",
                "server.port=8008", // Фиксированный порт для веб-приложения
                "server.servlet.context-path=/uhub"
        }
)
@AutoConfigureMockMvc
public class SecurityConfigTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean()
    private UserService userService;

    @Test
    @SneakyThrows
    public void userGetInfoByAuthorizationTest() {

        String userRoleToken = getUserRoleToken();

        Assertions.assertThat(userRoleToken == null || userRoleToken.isBlank()).isFalse();
        Assertions.assertThat(jwtTokenProvider.validateToken(userRoleToken)).isTrue();

        UserInfoDto userInfoDto = new UserInfoDto(
                "Name", "Lastname", "81234567890", "email@test.com", false, false, List.of(), List.of()
        );

        BDDMockito.given(userService.getUserInfo(any(UUID.class))).willReturn(userInfoDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/user/info/{userId}", new UUID(1L, 1L))
                                .header("Authorization", userRoleToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(user("test").roles("USER"))
                ).andDo(print())
                .andExpectAll(
                        status().isOk()
                );

        BDDMockito.then(userService).should(times(1)).getUserInfo(any());
        BDDMockito.then(userService).should(times(1)).getUserByUsername(any());

    }

    @SneakyThrows
    private String getUserRoleToken() {
        SignUpDto signUpDto = new SignUpDto(
                "Name", "Lastname", "81234567890", "Password1"
        );

        String signupDtoJson = objectMapper.writeValueAsString(signUpDto);

        BDDMockito.given(userService.getUserByUsername(any(String.class))).willReturn(UserModel.builder()
                .userId(new UUID(1L, 4L))
                .password("Password1")
                .name("Name")
                .build());

        return mockMvc.perform(
                        MockMvcRequestBuilders.post("/authorization/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(signupDtoJson)

                )
                .andDo(print())
                .andExpect(status().isOk())

                .andReturn().getResponse().getHeader("Authorization");
    }




}
