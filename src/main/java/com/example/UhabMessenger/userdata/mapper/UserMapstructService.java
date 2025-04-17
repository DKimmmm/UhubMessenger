package com.example.UhabMessenger.userdata.mapper;

import com.example.UhabMessenger.userdata.dto.register.SignUpDto;
import com.example.UhabMessenger.userdata.dto.user.UserInfoDto;
import com.example.UhabMessenger.userdata.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapstructService {

    UserModel toUserModel(SignUpDto dto);

    UserInfoDto toUserInfoDto(UserModel userModel);
}
