package com.example.UhabMessenger.userdata.mapper;

import com.example.UhabMessenger.userdata.dto.posts.PostDto;
import com.example.UhabMessenger.userdata.dto.posts.PostInfoDto;
import com.example.UhabMessenger.userdata.model.PostModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import javax.annotation.Nullable;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PostMapstructService {

    @Mapping(source = "title", target = "tittle")
    PostModel toPostModel(String title, String description);

    @Mapping(source = "tittle", target = "title")
    PostInfoDto toPostInfoDto(PostModel postModel);
}
