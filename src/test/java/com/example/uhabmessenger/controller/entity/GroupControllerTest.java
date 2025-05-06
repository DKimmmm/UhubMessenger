package com.example.uhabmessenger.controller.entity;

import com.example.uhabmessenger.dto.groups.GroupCreateDto;
import com.example.uhabmessenger.service.groups.GroupService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        request.andDo(MockMvcResultHandlers.print())
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

        request.andDo(MockMvcResultHandlers.print())
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

        request.andDo(MockMvcResultHandlers.print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.title").value("Получены данные некорректного формата, попробуйте снова.")
                );

        BDDMockito.then(groupService).should(times(0)).save(groupCreateDto);

    }



}
