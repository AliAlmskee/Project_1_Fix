package com.project1.fileSystem;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VideoMapper {

    @Mapping(source = "video", target = "video")
    @Mapping(source = "id", target = "id")
    VideoDTO toDto(Video video);
    Video toEntity(VideoDTO videoDTO);

}