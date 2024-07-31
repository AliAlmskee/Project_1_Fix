package com.project1.fileSystem;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocMapper {

    @Mapping(source = "doc", target = "doc")
    @Mapping(source = "id", target = "id")
    DocDTO toDto(Doc doc);
    Doc toEntity(DocDTO docDTO);

}