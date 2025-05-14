package com.example.uhabmessenger.dto.posts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostWithLikesInfoDto {

    private UUID postId;

    private String title;

    private String description;

    private List<UUID> imagesIds;

    private Integer likeCount;

    private Integer commentCount;

}
