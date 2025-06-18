package com.example.uhabmessenger.mapper;

import com.example.uhabmessenger.dto.groups.GroupCreateDto;
import com.example.uhabmessenger.dto.groups.GroupInfoDto;
import com.example.uhabmessenger.model.GroupModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GroupMapstructService {

    GroupInfoDto toInfoDto(GroupModel dto);

    GroupModel toModel(GroupCreateDto groupCreateDto);

}
