package com.example.uhabmessenger.dto.comment;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentInfoDto {

    private String text;
    private UUID userId;
    private String userName;
    private String userLastname;

}