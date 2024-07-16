package com.project1.projectProgress;

import com.project1.category.Category;
import com.project1.category.CategoryMapper;
import com.project1.profile.ClientProfile;
import com.project1.profile.ClientProfileMapper;
import com.project1.project.data.*;
import com.project1.skill.Skill;
import com.project1.skill.SkillMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {ClientProfileMapper.class, SkillMapper.class, CategoryMapper.class})
public abstract class ProjectProgressMapper {
    abstract ProjectResponse entityToResponse(Project project);

    abstract ProjectDetailsResponse entityToDetailsResponse(Project project);
    abstract List<ProjectResponse> entityToResponse(List<Project> project);

    @Mapping(source = "projectSkillIds", target = "projectSkill")
    @Mapping(source = "projectCategoriesIds", target = "projectCategories")
    @Mapping(source = "clientProfileId", target = "client")
    abstract Project toEntity(CreateProjectRequest createProjectRequest);

    ClientProfile idToClientProfile(Long id){
        return ClientProfile.builder().id(id).build();
    }
    Set<Skill> idsToSkills(Set<Long> ids){
        return ids.stream().map(aLong -> Skill.builder().id(aLong).build()).collect(Collectors.toSet());
    }

    Set<Category> idsToCategories(Set<Long> ids){
        return ids.stream().map(aLong -> Category.builder().id(aLong).build()).collect(Collectors.toSet());
    }

    @Mapping(target = "id", ignore = true)
    public abstract void updateFromDto(@MappingTarget Project project, UpdateProjectRequest updateProjectRequest);

}

