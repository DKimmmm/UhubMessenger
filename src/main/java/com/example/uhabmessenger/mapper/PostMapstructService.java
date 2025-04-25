package com.example.uhabmessenger.mapper;

import com.example.uhabmessenger.dto.posts.PostDto;
import com.example.uhabmessenger.dto.posts.PostInfoDto;
import com.example.uhabmessenger.model.PostModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PostMapstructService {

    PostModel toPostModel(PostDto dto);

    PostInfoDto toPostInfoDto(PostModel postModel);

}
