package com.example.uhabmessenger.dto.groups;

import java.util.List;
import java.util.UUID;

public record GroupCreateDto(

        String title,
        String description,
        List<UUID> userIds

) {
}
