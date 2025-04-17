package com.example.UhabMessenger.userdata.dto.posts;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostInfoDto {
    private String title;
    private String description;
    private List<UUID> imagesIds;
}
