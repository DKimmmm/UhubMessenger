package com.example.UhabMessenger.userdata.dto.user;

import lombok.*;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDto {
    private String name;
    private String lastname;
    private String phone;
    private String email;
    private Boolean approvedPhone;
    private Boolean approvedEmail;
    private List<UUID> imagesIds;

}
// Конструктор по умолчанию

