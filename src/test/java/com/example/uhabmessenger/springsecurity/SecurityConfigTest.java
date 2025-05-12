package com.example.uhabmessenger.springsecurity;

import com.example.uhabmessenger.bd.init.BaseIntegrationTest;
import com.example.uhabmessenger.dto.posts.PostInfoDto;
import com.example.uhabmessenger.dto.register.SignUpDto;
import com.example.uhabmessenger.dto.user.UserInfoDto;
import com.example.uhabmessenger.model.UserModel;
import com.example.uhabmessenger.service.PostService;
import com.example.uhabmessenger.service.groups.GroupService;
import com.example.uhabmessenger.service.user.other.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        properties = {
                "spring.profiles.active=test",
                "server.port=8008", // Фиксированный порт для веб-приложения
                "server.servlet.context-path=/uhab"
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

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private PostService postService;

    @MockitoBean
    private GroupService groupService;

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

    @Test
    @SneakyThrows
    public void userControllerUserGetInfoByAuthorizationTest() {

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
                        status().isOk(),
                        jsonPath("$.name", Matchers.is("Name")),
                        jsonPath("$.phone", Matchers.is("81234567890")),
                        jsonPath("$.approvedPhone", Matchers.is(false)),
                        jsonPath("$.imagesIds", Matchers.hasSize(0))
                );

        BDDMockito.then(userService).should(times(1)).getUserInfo(any());
        BDDMockito.then(userService).should(times(1)).getUserByUsername(any());

    }

    @Test
    @SneakyThrows
    public void userControllerUserGetInfoWithoutRoleTest() {

        UserInfoDto userInfoDto = new UserInfoDto(
                "Name", "Lastname", "81234567890", "email@test.com", false, false, List.of(), List.of()
        );

        BDDMockito.given(userService.getUserInfo(any(UUID.class))).willReturn(userInfoDto);

        ResultActions request = mockMvc.perform(
                MockMvcRequestBuilders.get("/user/info/{userId}", new UUID(1L, 1L))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        request.andDo(print())
                .andExpect(status().is(403));

        BDDMockito.then(userService).should(times(0)).getUserInfo(any());
        BDDMockito.then(userService).should(times(0)).getUserByUsername(any());

    }


    @Test
    @SneakyThrows
    public void postControllerGetPostInfoUserRoleTest() {

        UUID postId = new UUID(1L, 1L);
        PostInfoDto postInfoDto = new PostInfoDto(
                postId, "title", "description", List.of()
        );

        BDDMockito.given(postService.getPostInfo(any(UUID.class))).willReturn(postInfoDto);

        ResultActions request = mockMvc.perform(
                MockMvcRequestBuilders.get("/post/info/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("testuser").roles("USER"))
        );

        request.andDo(print())
                .andExpectAll(
                        status().is(200),
                        jsonPath("postId", Matchers.is(postId.toString())),
                        jsonPath("$.title", Matchers.is("title")),
                        jsonPath("$.imagesIds", Matchers.hasSize(0))

                );

        BDDMockito.then(postService).should(times(1)).getPostInfo(any());

    }


    @Test
    @SneakyThrows
    public void postControllerGetPostInfoBadRoleTest() {

        PostInfoDto postInfoDto = new PostInfoDto(
                new UUID(1L, 1L), "title", "description", List.of()
        );

        BDDMockito.given(postService.getPostInfo(any(UUID.class))).willReturn(postInfoDto);

        ResultActions request = mockMvc.perform(
                MockMvcRequestBuilders.get("/post/info/{postId}", new UUID(1L, 1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("test").roles("NOUSER"))
        );

        request.andDo(print())
                .andExpect(status().is(403));

        BDDMockito.then(postService).should(times(0)).getPostInfo(any());

    }


    @Test
    @SneakyThrows
    public void groupControllerGroupRemoveUserRoleTest() {

        BDDMockito.doNothing().when(groupService).removeById(any(UUID.class));

        ResultActions request = mockMvc.perform(
                MockMvcRequestBuilders.delete("/group/{groupId}", new UUID(1L, 1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("testuser").roles("USER"))
        );

        request.andDo(print())
                .andExpect(status().is(200));

        BDDMockito.then(groupService).should(times(1)).removeById(any());

    }


    @Test
    @SneakyThrows
    public void groupControllerGroupRemoveWithoutRoleTest() {

        BDDMockito.doNothing().when(groupService).removeById(any(UUID.class));

        ResultActions request = mockMvc.perform(
                MockMvcRequestBuilders.delete("/group/{groupId}", new UUID(1L, 1L))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        request.andDo(print())
                .andExpect(status().is(403));

        BDDMockito.then(groupService).should(times(0)).removeById(any());

    }




}
