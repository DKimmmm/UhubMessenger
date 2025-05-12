package com.example.uhabmessenger.service;

import com.example.uhabmessenger.dto.posts.PostInfoDto;
import com.example.uhabmessenger.mapper.PostMapstructService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostLineService {

    private final PostService postService;
    private final PostMapstructService postMapstructService;


    public List<PostInfoDto> getAllPostInfo() {

        return postService.createPostInfoDtosByPostModelList(
                postService.getAllPostList()
        );

    }

//    private List<PostInfoDto> listPostModelsToInfoDtoList(List<PostModel> allPostList) {
//
//        return allPostList.stream()
//                .map(postMapstructService::toPostInfoDto)
//                .toList();
//
//    }

}
