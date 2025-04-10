package com.example.UhabMessenger.authentication.mapper;

import com.example.UhabMessenger.authentication.dto.SignUpDto;
import com.example.UhabMessenger.authentication.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MapstructService {

//    MapstructService INSTANCE = Mappers.getMapper(MapstructService.class); //add instance to call mapper

    UserModel toUserModel(SignUpDto dto);

}
