package com.project1.fileSystem;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PhotoMapper {

    @Mapping(source = "photo", target = "photo")
    PhotoDTO toDto(Photo photo);
    Photo toEntity(PhotoDTO photoDTO);

}