package com.example.uhabmessenger.controller.entity;

import com.example.uhabmessenger.dto.posts.PostInfoDto;
import com.example.uhabmessenger.service.PostService;
import com.example.uhabmessenger.service.user.authorization.AuthUserService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(
        value = PostController.class,
        properties = {
                "spring.profiles.active=test",
                "server.port=8080",
                "server.servlet.context-path=/uhab"
        },
        excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @MockitoBean
    private AuthUserService authUserService;

    @Test
    @SneakyThrows
    void getInfoTest() {

        UUID postId = new UUID(1L, 1L);
        PostInfoDto value = new PostInfoDto(postId, "Title", "des", List.of());

        given(postService.getPostInfo(postId)).willReturn(value);

        ResultActions response = mockMvc.perform(get("/uhab")
                .uri("/post/info/{postId}", postId.toString())
                .contentType(MediaType.APPLICATION_JSON)

        );

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(

                        jsonPath("$.title", is("Title")),
                        jsonPath("$.description", is("des")),
                        jsonPath("$.imagesIds", hasSize(0))

                );

    }

//    @Test

}
