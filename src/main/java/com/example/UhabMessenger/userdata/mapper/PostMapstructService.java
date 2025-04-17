package com.example.UhabMessenger.userdata.mapper;

import com.example.UhabMessenger.userdata.dto.posts.PostDto;
import com.example.UhabMessenger.userdata.dto.posts.PostInfoDto;
import com.example.UhabMessenger.userdata.model.PostModel;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PostMapstructService {

    @Mapping(source = "title", target = "tittle")
    PostModel toPostModel(PostDto dto);

    @Mapping(source = "tittle", target = "title")
    PostInfoDto toPostInfoDto(PostModel postModel);
}
