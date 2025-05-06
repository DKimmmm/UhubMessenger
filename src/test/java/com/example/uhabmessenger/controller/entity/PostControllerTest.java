package com.example.uhabmessenger.controller.entity;

import com.example.uhabmessenger.dto.comment.AddCommentDto;
import com.example.uhabmessenger.dto.comment.CommentInfoDto;
import com.example.uhabmessenger.dto.posts.CreatePostDto;
import com.example.uhabmessenger.dto.posts.PostInfoDto;
import com.example.uhabmessenger.service.PostService;
import com.example.uhabmessenger.service.user.authorization.AuthUserService;
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
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
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
)
@AutoConfigureMockMvc(addFilters = false)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PostService postService;

    @MockitoBean
    private AuthUserService authUserService;

    @Test
    @SneakyThrows
    void getInfoTest() {

        UUID postId = new UUID(1L, 1L);
        PostInfoDto value = new PostInfoDto(postId, "Title", "des", List.of());

        BDDMockito.given(postService.getPostInfo(postId)).willReturn(value);

        ResultActions request = mockMvc.perform(
                MockMvcRequestBuilders.request(
                                HttpMethod.GET, "/post/info/{postId}", postId.toString()
                        )
                        .contentType(MediaType.APPLICATION_JSON)
        );

        request.andDo(print())
                .andExpectAll(

                        status().isOk(),

                        jsonPath("$.title", is("Title")),
                        jsonPath("$.description", is("des")),
                        jsonPath("$.imagesIds", hasSize(0))

                );

        BDDMockito.then(postService).should(times(1)).getPostInfo(postId);

    }

    @Test
    @SneakyThrows
    void getInfoBadRequestIncorrectIdTest() {

        BDDMockito.given(postService.getPostInfo(any())).willReturn(null);

        ResultActions request = mockMvc.perform(MockMvcRequestBuilders.get(
                                "/post/info/myuuid"
                        )
                        .contentType(MediaType.APPLICATION_JSON)
        );

        request.andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.detail").value("Method parameter 'postId': Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; Invalid UUID string: myuuid")
                );

        BDDMockito.then(postService).should(times(0)).getPostInfo(any());

    }


    @Test
    @SneakyThrows
    void userPostSaveTest() {

        UUID groupOrUserId = new UUID(2L, 2L);

        CreatePostDto createPostDto = new CreatePostDto(groupOrUserId, "title", null);

        MockMultipartFile imageFileFirstPart = new MockMultipartFile(
                "images",
                "test-imageFileFirstPart.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                new byte[0]
        );

        BDDMockito.doNothing().when(postService).userPostSave(any(CreatePostDto.class), any(List.class));

        MockPart dtoSecondPartJson = new MockPart("dto", objectMapper.writeValueAsString(createPostDto).getBytes());
        dtoSecondPartJson.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ResultActions request = mockMvc.perform(
                multipart("/post/user/create")

                        .file(imageFileFirstPart)
                        .part(dtoSecondPartJson)
                        .contentType(MediaType.MULTIPART_FORM_DATA)

        );

        request.andDo(print())
                .andExpectAll(status().isOk());

        then(postService).should(times(1)).userPostSave(any(CreatePostDto.class), any(List.class));
    }

    @Test
    @SneakyThrows
    void userPostSaveInvalidArgsIdIsNullTest() {

        CreatePostDto createPostDto = new CreatePostDto(null, "title", null);

        MockMultipartFile imageFileFirstPart = new MockMultipartFile(
                "images",
                "test-imageFileFirstPart.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                new byte[0]
        );

        BDDMockito.doNothing().when(postService).userPostSave(any(CreatePostDto.class), any(List.class));

        MockPart dtoJsonSecondPart = new MockPart("dto", objectMapper.writeValueAsString(createPostDto).getBytes());
        dtoJsonSecondPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ResultActions perform = mockMvc
                .perform(multipart(HttpMethod.POST, "/post/user/create")

                        .file(imageFileFirstPart)
                        .part(dtoJsonSecondPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA));

        perform.andDo(print())
                .andExpectAll(status().isBadRequest());

        BDDMockito.then(postService).should(times(0)).userPostSave(any(CreatePostDto.class), any(List.class));

    }

    @Test
    @SneakyThrows
    void userPostSaveGoodArgsOnlyImageTest() {

        UUID uuid = new UUID(2L, 2L);

        CreatePostDto createPostDto = new CreatePostDto(uuid, null, null);
        String dtoJsonValue = objectMapper.writeValueAsString(createPostDto);

        MockPart dtoPart = new MockPart("dto", dtoJsonValue.getBytes());
        dtoPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        MockMultipartFile imageSecondPartFile = new MockMultipartFile(
                "images",
                "test-imageSecondPartFile.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "moreBytesInsteadImage".getBytes()
        );

        BDDMockito.doNothing().when(postService).userPostSave(any(CreatePostDto.class), any(List.class));

        ResultActions perform1 = mockMvc
                .perform(multipart(HttpMethod.POST, "/post/user/create")

                        .file(imageSecondPartFile)
                        .part(dtoPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA));

        perform1.andDo(print())
                .andExpectAll(status().isOk());

        BDDMockito.then(postService).should(times(1)).userPostSave(any(CreatePostDto.class), any(List.class));

    }

    @Test
    @SneakyThrows
    void userGroupSaveSuccessfulTest() {
        BDDMockito.doNothing().when(postService).groupPostSave(any(CreatePostDto.class), any(List.class));

        UUID uuidForCreatePostDto = new UUID(1L, 1L);
        CreatePostDto createPostDto = new CreatePostDto(uuidForCreatePostDto, "title", "description");
        String createPostDtoJsonValue = objectMapper.writeValueAsString(createPostDto);

        MockPart dtoPart = new MockPart("dto", createPostDtoJsonValue.getBytes());
        dtoPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        MockMultipartFile imagePartFile = new MockMultipartFile(
                "images",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "sdfsdfsd".getBytes()
        );

        ResultActions request = mockMvc.perform(
                MockMvcRequestBuilders.multipart(
                                HttpMethod.POST, "/post/group/create"
                        )
                        .file(imagePartFile)
                        .file(imagePartFile)
                        .part(dtoPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        );

        request.andDo(print())
                .andExpectAll(
                        status().is(200)
                );

        BDDMockito.then(postService).should(times(1)).groupPostSave(any(CreatePostDto.class), any(List.class));

    }

    @Test
    @SneakyThrows
    void postGroupSaveWithoutImageShouldBeGoodTest() {
        BDDMockito.doNothing().when(postService).groupPostSave(any(CreatePostDto.class), any(List.class));

        UUID uuidForCreatePostDto = new UUID(1L, 1L);

        CreatePostDto createPostDto = new CreatePostDto(uuidForCreatePostDto, "title", null);
        String dtoJsonValue = objectMapper.writeValueAsString(createPostDto);

        MockPart dtoPart = new MockPart("dto", dtoJsonValue.getBytes());
        dtoPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ResultActions request = mockMvc.perform(
                MockMvcRequestBuilders.multipart(
                                HttpMethod.POST, "/post/group/create"
                        )

                        .part(dtoPart)
        );

        request.andDo(print())
                .andExpectAll(
                        status().is(200)
                );

        BDDMockito.then(postService).should(times(1)).groupPostSave(any(CreatePostDto.class), any());

    }

    @Test
    @SneakyThrows
    void addCommentTest() {

        BDDMockito.doNothing().when(postService).addComment(any(AddCommentDto.class));

        AddCommentDto addCommentDto = new AddCommentDto(
                new UUID(1L, 1L),
                new UUID(2L, 2L),
                "comment text"
        );

        String dtoJsonValue = objectMapper.writeValueAsString(addCommentDto);

        ResultActions request = mockMvc.perform(
                MockMvcRequestBuilders.post("/post/add/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoJsonValue)
        );

        request.andDo(print())
                .andExpectAll(
                        status().isOk()
                );

        BDDMockito.then(postService).should(times(1)).addComment(any());

    }

    @Test
    @SneakyThrows
    void addCommentBadRequestSizeTextLittleTest() {

        BDDMockito.doNothing().when(postService).addComment(any(AddCommentDto.class));

        AddCommentDto addCommentDto = new AddCommentDto(
                new UUID(1L, 1L),
                new UUID(2L, 2L),
                "c"
        );

        String dtoJsonValue = objectMapper.writeValueAsString(addCommentDto);

        ResultActions request = mockMvc.perform(
                MockMvcRequestBuilders.post("/post/add/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoJsonValue)
        );

        request.andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.title", is("Получены данные некорректного формата, попробуйте снова."))
                );

        BDDMockito.then(postService).should(times(0)).addComment(any());

    }

    @Test
    @SneakyThrows
    void getPostCommentsTest() {

        UUID userId = new UUID(12L, 12L);
        List<CommentInfoDto> responseDto = List.of(
                new CommentInfoDto("text", userId, "username", "userLastname"),
                new CommentInfoDto("text1", new UUID(121L, 121L), "username1", "userLastname1")
        );

        BDDMockito.given(postService.getCommentsByPostId(any())).willReturn(responseDto);

        ResultActions request = mockMvc.perform(
                MockMvcRequestBuilders.get("/post/get-comments")
                        .param("postId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
        );

        request.andDo(print())
                .andExpectAll(
                        status().isOk(),

                        jsonPath("$", hasSize(2)),
                        jsonPath("$[0].text", is("text")),
                        jsonPath("$[0].userId", is("00000000-0000-000c-0000-00000000000c")),
                        jsonPath("$[0].userName", is("username")),
                        jsonPath("$[0].userLastname", is("userLastname")),
                        jsonPath("$[1].text", is("text1"))
                );

        BDDMockito.then(postService).should(times(1)).getCommentsByPostId(any());

    }

    @Test
    @SneakyThrows
    void imageDownloadTest() {

        BDDMockito.doNothing().when(postService).imageDownload(any(UUID.class), any(HttpServletResponse.class));

        UUID imageId = new UUID(3L, 3L);

        ResultActions request = mockMvc.perform(
                MockMvcRequestBuilders.get("/post/image/download/{imageId}", imageId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
        );

        request.andDo(print())
                .andExpectAll(
                        status().isOk()
                );

        BDDMockito.then(postService).should(times(1)).imageDownload(any(), any());

    }

}

