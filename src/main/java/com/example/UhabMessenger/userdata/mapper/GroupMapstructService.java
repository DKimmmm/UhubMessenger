package com.example.UhabMessenger.userdata.mapper;

import com.example.UhabMessenger.userdata.dto.groups.GroupInfoDto;
import com.example.UhabMessenger.userdata.model.GroupModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GroupMapstructService {

    GroupInfoDto toInfoDto(GroupModel dto);

}
