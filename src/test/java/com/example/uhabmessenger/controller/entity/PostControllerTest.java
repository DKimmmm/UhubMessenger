package com.example.uhabmessenger.controller.entity;

import com.example.uhabmessenger.dto.posts.CreatePostDto;
import com.example.uhabmessenger.dto.posts.PostInfoDto;
import com.example.uhabmessenger.service.PostService;
import com.example.uhabmessenger.service.user.authorization.AuthUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(
        value = PostController.class,
        properties = {
                "spring.profiles.active=test",
                "server.port=8080",
                "server.servlet.context-path=/uhab"
        }
//        excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
@AutoConfigureMockMvc(addFilters = false)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @MockitoBean
    private AuthUserService authUserService;

    @Autowired
    private ObjectMapper objectMapper;

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

    @Test
    @SneakyThrows
    void userPostSaveTest() {
        // Arrange
        UUID groupOrUserId = new UUID(2L, 2L);
        CreatePostDto createPostDto = new CreatePostDto(groupOrUserId, "title", null);
        MockMultipartFile image = new MockMultipartFile(
                "images",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                new byte[0]
        );

        BDDMockito.doNothing().when(postService).userPostSave(any(CreatePostDto.class), any(List.class));

        // Act
        MockPart dtoPart = new MockPart("dto", objectMapper.writeValueAsString(createPostDto).getBytes());
        dtoPart.getHeaders().setContentType(MediaType.APPLICATION_JSON); // Указываем Content-Type для dto

        ResultActions perform = mockMvc.perform(multipart("/post/user/create")
                .file(image) // Отправляем images как MultipartFile
                .part(dtoPart) // Отправляем dto как JSON
                .contentType(MediaType.MULTIPART_FORM_DATA));

        // Assert
        perform.andDo(print())
                .andExpectAll(status().isOk());

        then(postService).should().userPostSave(any(CreatePostDto.class), any(List.class));
    }

    @Test
    @SneakyThrows
    void userPostSaveUnValidArgsIdNotNullTest() {
        // Arrange
        CreatePostDto createPostDto = new CreatePostDto(null, "title", null);
        MockMultipartFile image = new MockMultipartFile(
                "images",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                new byte[0]
        );

        BDDMockito.doNothing().when(postService).userPostSave(any(CreatePostDto.class), any(List.class));

        // Act
        MockPart dtoPart = new MockPart("dto", objectMapper.writeValueAsString(createPostDto).getBytes());
        dtoPart.getHeaders().setContentType(MediaType.APPLICATION_JSON); // Указываем Content-Type для dto

        ResultActions perform = mockMvc.perform(multipart("/post/user/create")
                .file(image) // Отправляем images как MultipartFile
                .part(dtoPart) // Отправляем dto как JSON
                .contentType(MediaType.MULTIPART_FORM_DATA));

        // Assert
        perform.andDo(print())
                .andExpectAll(status().isBadRequest());

    }



    @Test
    @SneakyThrows
    void userPostSaveGoodArgsOnlyImageTest() {

        UUID uuid = new UUID(2L, 2L);

        CreatePostDto createPostDto = new CreatePostDto(uuid, null, null);
        String s = objectMapper.writeValueAsString(createPostDto);
        System.out.println("----------------------"+ s);
        MockPart dtoPart = new MockPart("dto", s.getBytes());
        dtoPart.getHeaders().setContentType(MediaType.APPLICATION_JSON); // Указываем Content-Type для dto

        // Arrange

        MockMultipartFile image = new MockMultipartFile(
                "images",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "moreBytesInsteadImage".getBytes()
        );

        BDDMockito.doNothing().when(postService).userPostSave(any(CreatePostDto.class), any(List.class));

        ResultActions perform1 = mockMvc.perform(multipart("/post/user/create")
                .file(image) // Отправляем images как MultipartFile
                .part(dtoPart) // Отправляем dto как JSON
                .contentType(MediaType.MULTIPART_FORM_DATA));

        perform1.andDo(print())
                .andExpectAll(status().isOk());
    }
}
