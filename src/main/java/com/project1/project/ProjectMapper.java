package com.project1.project;

import com.project1.profile.ClientProfile;
import com.project1.profile.ClientProfileDTO;
import com.project1.profile.ClientProfileMapper;
import com.project1.project.data.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = ClientProfileMapper.class)
public abstract class ProjectMapper {
    abstract ProjectResponse entityToResponse(Project project);

    abstract ProjectDetailsResponse entityToDetailsResponse(Project project);
    abstract List<ProjectResponse> entityToResponse(List<Project> project);

    abstract Project toEntity(CreateProjectRequest createProjectRequest);

    @Mapping(target = "id", ignore = true)
    public abstract void updateFromDto(@MappingTarget Project project, UpdateProjectRequest updateProjectRequest);

}

