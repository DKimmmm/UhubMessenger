package com.example.uhabmessenger.service.post;

import com.example.uhabmessenger.dto.posts.PostWithLikesInfoDto;
import com.example.uhabmessenger.mapper.PostMapstructService;
import com.example.uhabmessenger.model.ImageModel;
import com.example.uhabmessenger.model.PostModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostLineService {

    private final SimplePostService simplePostService;

    private final PostMapstructService postMapstructService;


    public List<PostWithLikesInfoDto> getAllPostInfo() {

        return createPostWithLikeInfoDtosByPostModelList(
                simplePostService.findAllByRemoveMark(false)
        );

    }

    public List<PostWithLikesInfoDto> getAllPostInfoBySpecification(Integer pageSize, Boolean isSortedByLikes, Boolean isSortedByComments, String searchedBy) {

        Pageable pageable = PageRequest.of(0, pageSize);

        Specification<PostModel> specification = PostSpecifications.withFilters(
                isSortedByLikes, isSortedByComments, searchedBy
        );

        List<PostModel> allBySpecification =
                simplePostService.getAllBySpecification(specification, pageable);

        return createPostWithLikeInfoDtosByPostModelList(allBySpecification);

    }

    public List<PostWithLikesInfoDto> createPostWithLikeInfoDtosByPostModelList(List<PostModel> allPostList) {

        return allPostList.stream()
                .map(this::modelToWithLikesInfoDtoWithListImageIds)
                .toList();

    }

    private PostWithLikesInfoDto modelToWithLikesInfoDtoWithListImageIds(PostModel postModel) {

        PostWithLikesInfoDto postWithLikesInfoDto = postMapstructService.toPostWithLikesInfoDto(postModel);

        postWithLikesInfoDto.setLikeCount(postModel.getPostLikes().size());

        postWithLikesInfoDto.setCommentCount(postModel.getComments().size());

        return addImagesIdsToPostWithLikesInfoDto(
                postModel,
                postWithLikesInfoDto
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
