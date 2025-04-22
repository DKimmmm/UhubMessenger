package com.example.uhabmessenger.dto.groups;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupInfoDto {

    private UUID groupId;
    private String title;
    private String description;
    private List<UUID> userIds;
    private List<UUID> photoIds;

}
