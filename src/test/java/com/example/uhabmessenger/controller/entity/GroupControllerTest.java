package com.example.uhabmessenger.controller.entity;

import com.example.uhabmessenger.dto.groups.GroupCreateDto;
import com.example.uhabmessenger.dto.groups.GroupInfoDto;
import com.example.uhabmessenger.dto.posts.PostInfoDto;
import com.example.uhabmessenger.service.groups.GroupService;
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

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        value = GroupController.class,
        properties = {
                "spring.profiles.active=test",
                "server.port=8008",
                "server.servlet.context-path=/api"
        }
)
@AutoConfigureMockMvc(addFilters = false)
public class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GroupService groupService;

    @Test
    @SneakyThrows
    void createGroupTest() {

        BDDMockito.doNothing().when(groupService).save(any(GroupCreateDto.class));

        GroupCreateDto groupCreateDto = new GroupCreateDto("ttitle", "description", null);
        String DtoJsonValue = objectMapper.writeValueAsString(groupCreateDto);

        ResultActions request = mockMvc.perform(
                MockMvcRequestBuilders.post("/group")
                        .content(DtoJsonValue)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        request.andDo(print())
                .andExpect(status().isOk());

        BDDMockito.then(groupService).should(times(1)).save(groupCreateDto);

    }

    @Test
    @SneakyThrows
    void createGroupNullTitleBadRequestTest() {

        BDDMockito.doNothing().when(groupService).save(any(GroupCreateDto.class));

        GroupCreateDto groupCreateDto = new GroupCreateDto(null, "description", null);
        String DtoJsonValue = objectMapper.writeValueAsString(groupCreateDto);

        ResultActions request = mockMvc.perform(
                MockMvcRequestBuilders.post("/group")
                        .content(DtoJsonValue)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        request.andDo(print())
                .andExpect(status().isBadRequest());

        BDDMockito.then(groupService).should(times(0)).save(groupCreateDto);

    }

    @Test
    @SneakyThrows
    void createGroupLittleDescriptionBadRequestTest() {

        BDDMockito.doNothing().when(groupService).save(any(GroupCreateDto.class));

        GroupCreateDto groupCreateDto = new GroupCreateDto("Title", "des", null);
        String DtoJsonValue = objectMapper.writeValueAsString(groupCreateDto);

        ResultActions request = mockMvc.perform(
                MockMvcRequestBuilders.post("/group")
                        .content(DtoJsonValue)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        request.andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.title").value("Получены данные некорректного формата, попробуйте снова.")
                );

        BDDMockito.then(groupService).should(times(0)).save(groupCreateDto);

    }

    @Test
    @SneakyThrows
    void photoUpdateTest() {

        BDDMockito.doNothing().when(groupService).photoUpdate(any(UUID.class), any(MultipartFile.class));

        MockMultipartFile imageFilePart = new MockMultipartFile(
                "multipartFile",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "skdfjlsdf".getBytes()
        );

        ResultActions request = mockMvc.perform(
                MockMvcRequestBuilders.multipart(HttpMethod.POST, "/group/photo-update/{groupId}", new UUID(3L, 3L))
                        .file(imageFilePart)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        );

        request.andDo(print())
                .andExpect(status().isOk());

        BDDMockito.then(groupService).should(times(1)).photoUpdate(any(), any());

    }

    @Test
    @SneakyThrows
    void photoUpdateFileIsNullBadRequestTest() {

        BDDMockito.doNothing().when(groupService).photoUpdate(any(UUID.class), any(MultipartFile.class));

        ResultActions request = mockMvc.perform(
                MockMvcRequestBuilders.multipart(HttpMethod.POST, "/group/photo-update/{groupId}", new UUID(3L, 3L))
        );

        request.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Получены данные некорректного формата, попробуйте снова."));

        BDDMockito.then(groupService).should(times(0)).photoUpdate(any(), any());

    }

    @Test
    @SneakyThrows
    void getAllInfoTest() {

        List<GroupInfoDto> responseDto = List.of(
                new GroupInfoDto(new UUID(2L, 3L), "title", "des", List.of(), List.of()),
                new GroupInfoDto(new UUID(1L, 3L), "title11", "des11", List.of(), List.of())
        );

        BDDMockito.given(groupService.getAllInfo()).willReturn(responseDto);

        ResultActions request = mockMvc.perform(
                MockMvcRequestBuilders.get("/group/all-info")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        request.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$", hasSize(2)),
                        jsonPath("$[0].title").value("title"),
                        jsonPath("$[1].description").value("des11"),
                        jsonPath("$[0].userIds", empty()),
                        jsonPath("$[0].photoIds", empty())
                );

        BDDMockito.then(groupService).should(times(1)).getAllInfo();

    }

    @Test
    @SneakyThrows
    void getInfoTest() {

        UUID uuid = new UUID(3L, 34L);

        GroupInfoDto groupInfoDto = new GroupInfoDto(uuid, "title", "des", List.of(), List.of());

        BDDMockito.given(groupService.getInfo(uuid)).willReturn(groupInfoDto);

        ResultActions request = mockMvc.perform(
                MockMvcRequestBuilders.get("/group/info/{groupId}", uuid.toString())
                        .contentType(MediaType.APPLICATION_JSON)
        );

        request.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.groupId").value(uuid.toString()),
                        jsonPath("$.photoIds", empty())
                );

        BDDMockito.then(groupService).should(times(1)).getInfo(any());

    }

    @Test
    @SneakyThrows
    void addMembersTest() {

        UUID uuid = new UUID(2L, 3L);

        BDDMockito.doNothing().when(groupService).addMembers(any(UUID.class), any(List.class));

        List<UUID> membersIds = List.of(
                new UUID(1L, 3L),
                new UUID(2L, 3L),
                new UUID(3L, 3L)
        );

        String dtoListJsonValue = objectMapper.writeValueAsString(membersIds);

        ResultActions request = mockMvc.perform(
                MockMvcRequestBuilders.post("/group/add-members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("groupId", uuid.toString())
                        .content(dtoListJsonValue)
        );

        request.andDo(print())
                .andExpectAll(
                        status().isOk()
                );

        BDDMockito.then(groupService).should(times(1)).addMembers(any(), any());

    }

    @Test
    @SneakyThrows
    void addMembersWithoutBodyBadRequestTest() {

        UUID uuid = new UUID(2L, 3L);

        BDDMockito.doNothing().when(groupService).addMembers(any(UUID.class), any(List.class));

        ResultActions request = mockMvc.perform(
                MockMvcRequestBuilders.post("/group/add-members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("groupId", uuid.toString())
        );

        request.andDo(print())
                .andExpect(status().isBadRequest());

        BDDMockito.then(groupService).should(times(0)).addMembers(any(), any());

    }

    @Test
    @SneakyThrows
    void removeMemberTest() {

        BDDMockito.doNothing().when(groupService).removeMember(any(UUID.class), any(UUID.class));

        UUID groupId = new UUID(2L, 3L);
        UUID memberId = new UUID(1L, 2L);

        ResultActions request = mockMvc.perform(
                MockMvcRequestBuilders.delete("/group/member")
                        .param("groupId", groupId.toString())
                        .param("memberId", memberId.toString())
        );

        request.andDo(print())
                .andExpectAll(
                        status().isOk()
                );

        BDDMockito.then(groupService).should(times(1)).removeMember(any(), any());

    }

    @Test
    @SneakyThrows
    void groupRemoveTest() {

        BDDMockito.doNothing().when(groupService).removeById(any(UUID.class));

        UUID groupId = new UUID(2L, 3L);

        ResultActions request = mockMvc.perform(
                MockMvcRequestBuilders.delete("/group/{groupId}", groupId.toString())
        );

        request.andDo(print())
                .andExpect(status().isOk());

        BDDMockito.then(groupService).should(times(1)).removeById(any());

    }

    @Test
    @SneakyThrows
    void downloadImageTest() {

        BDDMockito.doNothing().when(groupService).downloadImage(any(UUID.class), any(UUID.class), any(HttpServletResponse.class));

        UUID groupId = new UUID(2L, 3L);
        UUID imageId = new UUID(1L, 2L);

        ResultActions request = mockMvc.perform(
                MockMvcRequestBuilders.get("/group/image/download")
                        .param("groupId", groupId.toString())
                        .param("imageId", imageId.toString())
        );

        request.andDo(print())
                .andExpect(status().isOk());

        BDDMockito.then(groupService).should(times(1)).downloadImage(any(), any(), any());

    }

    @Test
    @SneakyThrows
    void groupPostInfoDtoTest() {

        List<PostInfoDto> responseDto = List.of(
                new PostInfoDto(new UUID(1L, 1L), "title", "des", List.of()),
                new PostInfoDto(new UUID(2L, 2L), "title1", "des1", List.of())
        );

        BDDMockito.given(groupService.getPostsInfoDto(any(UUID.class))).willReturn(responseDto);

        UUID uuid = new UUID(3L, 3L);

        ResultActions request = mockMvc.perform(
                MockMvcRequestBuilders.get("/group/posts/{groupId}", uuid.toString())
                        .contentType(MediaType.APPLICATION_JSON)
        );

        request.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$", hasSize(2)),
                        jsonPath("$[0].title").value("title"),
                        jsonPath("$[1].description").value("des1"),
                        jsonPath("$[0].imagesIds", empty())
                );

        BDDMockito.then(groupService).should(times(1)).getPostsInfoDto(any());

    }

    @Test
    @SneakyThrows
    void groupPostInfoDtoNullResultShouldGoodRequestTest() {

        BDDMockito.given(groupService.getPostsInfoDto(any(UUID.class))).willReturn(null);

        UUID uuid = new UUID(3L, 3L);

        ResultActions request = mockMvc.perform(
                MockMvcRequestBuilders.get("/group/posts/{groupId}", uuid.toString())
        );

        request.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().string("")
                );

        BDDMockito.then(groupService).should(times(1)).getPostsInfoDto(any());

    }

}
