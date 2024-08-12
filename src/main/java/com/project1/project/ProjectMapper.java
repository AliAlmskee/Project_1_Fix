package com.project1.project;

import com.project1.category.Category;
import com.project1.category.CategoryMapper;
import com.project1.profile.ClientProfileMapper;
import com.project1.project.data.*;
import com.project1.skill.Skill;
import com.project1.skill.SkillMapper;
import com.project1.user.User;
import com.project1.user.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {ClientProfileMapper.class, SkillMapper.class, CategoryMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ProjectMapper {
    public abstract ProjectResponse entityToResponse(Project project);
    public abstract ProjectDetailsResponse entityToDetailsResponse(Project project);
    public abstract List<ProjectDetailsResponse> entityToDetailsResponse(List<Project> projects);
    @Mapping(source = "projectSkillIds", target = "projectSkill")
    @Mapping(source = "projectCategoryId", target = "projectCategory")
    @Mapping(source = "clientProfileId", target = "client")
    abstract Project toEntity(CreateProjectRequest createProjectRequest);

    Set<Skill> idsToSkills(Set<Long> ids){
        return ids.stream().map(aLong -> Skill.builder().id(aLong).build()).collect(Collectors.toSet());
    }

    Set<Category> idsToCategories(Set<Long> ids){
        return ids.stream().map(aLong -> Category.builder().id(aLong).build()).collect(Collectors.toSet());
    }

    public abstract List<ProjectResponse> projectsToProjectResponses(List<Project> projects);


    @Mapping(target = "id", ignore = true)
    @Mapping(source = "projectCategoryId", target = "projectCategory")
    @Mapping(source = "projectSkillIds", target = "projectSkill")
    public abstract void updateFromDto(@MappingTarget Project project, UpdateProjectRequest updateProjectRequest);
}

