package com.project1.project;

import com.project1.auditing.ApplicationAuditAware;
import com.project1.category.Category;
import com.project1.profile.ClientProfile;
import com.project1.profile.WorkerProfile;
import com.project1.project.data.*;
import com.project1.skill.Skill;
import com.project1.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectMapper projectMapper;
    private final ProjectRepository projectRepository;
    private final ApplicationAuditAware auditAware;

    public List<ProjectResponse> getAllFiltered(String search, List<Category> categories, List<Skill> skills, Long minBudget, Long maxBudget, Long duration, ProjectStatus status,ProjectSortTypes sortBy, Boolean sortDes) {
        //TODO: merge 3 features? query not done.
        return projectMapper.entityToResponse(projectRepository.findAll());
    }

    public ProjectDetailsResponse get(Long id) {
        return projectMapper.entityToDetailsResponse(projectRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project Not Found")));
    }

    public List<ProjectResponse> getByUser(Integer id) {
        // TODO: do merge profiles (worker and client too)? if so do grouping?
        final User user = User.builder().id(id).build();
        return projectMapper.entityToResponse(projectRepository.findAllByClientOrWorker(user, user));
    }

    public ProjectResponse create(CreateProjectRequest createProjectRequest) {
        //TODO test id entity trick (update, updateInternal, getByUser)
        Integer userId = auditAware.getCurrentAuditor().orElseThrow(() -> new RuntimeException("Auditor ID not found"));
        Project project = projectMapper.toEntity(createProjectRequest);
        project.setClient(ClientProfile.builder().id(createProjectRequest.clientProfileId()).build());
        project.setStatus(ProjectStatus.open);
        project.setProjectCategories(createProjectRequest.projectCategoriesIds().stream().map(id -> Category.builder().id(id).build()).collect(Collectors.toSet()));
        project.setProjectSkill(createProjectRequest.projectSkillIds().stream().map(id -> Skill.builder().id(id).build()).collect(Collectors.toSet()));
        return projectMapper.entityToResponse(projectRepository.save(project));
    }
    public ProjectDetailsResponse update(Long projectId, UpdateProjectRequest updateProjectRequest) throws ResponseStatusException{
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project Not Found"));
        projectMapper.updateFromDto(project, updateProjectRequest);
        if(project.getProjectCategories() != null){
            project.setProjectCategories(updateProjectRequest.projectCategoriesIds().stream().map(id -> Category.builder().id(id).build()).collect(Collectors.toSet()));
        }
        if(project.getProjectSkill() != null) {
            project.setProjectSkill(updateProjectRequest.projectSkillIds().stream().map(id -> Skill.builder().id(id).build()).collect(Collectors.toSet()));
        }
        return projectMapper.entityToDetailsResponse(projectRepository.save(project));
    }

    public Map<String, String> delete(Long projectId) throws ResponseStatusException{
        Integer userId = auditAware.getCurrentAuditor().orElseThrow(() -> new RuntimeException("Auditor ID not found"));
        boolean deleted = projectRepository.deleteByIdAndStatusAndClient(projectId, ProjectStatus.open, User.builder().id(userId).build());
//        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project Not Found"));
//        if(project.getStatus().equals(ProjectStatus.open)){
        if(deleted){
//            projectRepository.delete(project);
            return Map.of("message", "Project Deleted");
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot Delete project after it settled.");
    }

    //-------ADMIN--------
    public Map<String, String> adminDelete(Long projectId){
        projectRepository.deleteById(projectId);
        return Map.of("message", "Project Deleted.");
    }


    //-------Internal--------

    //for updating status in progress
    public Project updateInternal(Long projectId, ProjectStatus projectStatus, Long workerId) throws ResponseStatusException{
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project Not Found"));
        if(projectStatus != null){
            project.setStatus(projectStatus);
        }
        if(workerId != null){
            project.setWorker(WorkerProfile.builder().id(workerId).build());
        }
        return projectRepository.save(project);
    }
}
