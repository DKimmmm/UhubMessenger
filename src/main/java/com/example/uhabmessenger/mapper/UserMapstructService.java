package com.example.uhabmessenger.mapper;

import com.example.uhabmessenger.dto.register.SignUpDto;
import com.example.uhabmessenger.dto.user.UserInfoDto;
import com.example.uhabmessenger.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapstructService {

    UserModel toUserModel(SignUpDto dto);

    UserInfoDto toUserInfoDto(UserModel userModel);

}
