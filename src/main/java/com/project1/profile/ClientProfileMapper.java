package com.project1.profile;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ClientProfileMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user", target = "userDTO")
    @Mapping(source = "jobTitle", target = "jobTitleDTO")
    @Mapping(source = "photos", target = "photoDTOs")
    @Mapping(source = "skills", target = "skillDTOs")
    @Mapping(source = "id", target = "id")
    ClientProfileDTO toDto(ClientProfile clientProfile);

    ClientProfile toEntity(ClientProfileDTO clientProfileDTO);
    ClientProfile toEntity(ClientProfileRequest clientProfileRequest);

}