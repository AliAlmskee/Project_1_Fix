package com.project1.skill;


import com.project1.fileSystem.Photo;
import com.project1.fileSystem.PhotoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface SkillMapper {
    @Mapping(source = "name", target = "name")
    @Mapping(source = "id", target = "id")
    SkillDTO toDto(Skill skill);
    Skill toEntity(SkillDTO skillDTO);
}
