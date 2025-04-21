package com.example.uhabmessenger.mapper;

import com.example.uhabmessenger.dto.posts.PostDto;
import com.example.uhabmessenger.dto.posts.PostInfoDto;
import com.example.uhabmessenger.model.PostModel;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PostMapstructService {

    @Mapping(source = "title", target = "tittle")
    PostModel toPostModel(PostDto dto);

    @Mapping(source = "tittle", target = "title")
    PostInfoDto toPostInfoDto(PostModel postModel);
}
