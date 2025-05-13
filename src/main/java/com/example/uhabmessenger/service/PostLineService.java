package com.example.uhabmessenger.service;

import com.example.uhabmessenger.dto.posts.PostWithLikesInfoDto;
import com.example.uhabmessenger.mapper.PostMapstructService;
import com.example.uhabmessenger.model.ImageModel;
import com.example.uhabmessenger.model.PostModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostLineService {

    private final PostService postService;
    private final PostMapstructService postMapstructService;


    public List<PostWithLikesInfoDto> getAllPostInfo() {

        return createPostWithLikeInfoDtosByPostModelList(
                postService.getAllPostList()
        );

    }

    public List<PostWithLikesInfoDto> createPostWithLikeInfoDtosByPostModelList(List<PostModel> allPostList) {

        return allPostList.stream()
                .map(this::modelToWithLikesInfoDtoWithListImageIds)
                .toList();

    }

    private PostWithLikesInfoDto modelToWithLikesInfoDtoWithListImageIds(PostModel postModel) {

        return addImagesIdsToPostWithLikesInfoDto(
                postModel,
                postMapstructService.toPostWithLikesInfoDto(postModel)
        );

    }

    private PostWithLikesInfoDto addImagesIdsToPostWithLikesInfoDto(PostModel postModel, PostWithLikesInfoDto postWithLikesInfoDto) {

        postWithLikesInfoDto.setImagesIds(
                getImagesIdsFromPostModel(postModel)
        );

        return postWithLikesInfoDto;

    }

    private List<UUID> getImagesIdsFromPostModel(PostModel postModel) {

        return postModel.getImages().stream()
                .map(ImageModel::getImageId)
                .toList();

    }

}
