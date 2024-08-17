package com.project1.project;

import com.project1.auditing.ApplicationAuditAware;
import com.project1.category.Category;
import com.project1.offer.OfferRepository;
import com.project1.offer.data.Offer;
import com.project1.offer.data.OfferStatus;
import com.project1.profile.ClientProfile;
import com.project1.profile.WorkerProfile;
import com.project1.project.data.*;
import com.project1.skill.Skill;
import com.project1.transaction.TransactionService;
import com.project1.user.Permission;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectMapper projectMapper;
    private final ProjectRepository projectRepository;
    private final OfferRepository offerRepository;
    private final ApplicationAuditAware auditAware;
    private final TransactionService transactionService;
    @PersistenceContext
    private EntityManager entityManager;
//    public List<ProjectWithOfferCountResponse> getAllFiltered(
//            String search, List<Long> categories,
//            List<Long> skills, Long minBudget,
//            Long maxBudget, Long duration,
//            ProjectStatus status, ProjectSortTypes sortBy,
//            Boolean sortDes) {
//        return projectMapper.entityWithOffersToResponse(projectRepository.findFilteredProjects(search, categories, skills, minBudget, maxBudget, duration, status, sortBy.name(), sortDes? "DESC" : "ASC"));
//    }

//    public List<ProjectWithOfferCountResponse> getAllFiltered(
//            String search, List<Long> categories,
//            List<Long> skills, Long minBudget,
//            Long maxBudget, Long duration,
//            ProjectStatus status, ProjectSortTypes sortBy,
//            Boolean sortDes) {
//        return projectMapper.entityWithOffersToResponse(projectRepository.findFilteredProjects(search, categories, skills, minBudget, maxBudget, duration, status, sortBy.name(), sortDes? "DESC" : "ASC"));
//    }

    public List<ProjectDetailsResponse> getFilteredProjects(String namePattern, List<Long> categoryIds, List<Long> skillIds,
                                                            Long minBudget, Long maxBudget, Long duration, ProjectStatus status,
                                                            ProjectSortTypes sortBy, Boolean sortDes) {
        List<Project> projects = projectRepository.findAll();


        // Filter by name pattern
        if(namePattern!=null) {
            projects = projects.stream()
                    .filter(project -> project.getName() != null && project.getName().contains(namePattern))
                    .collect(Collectors.toList());
        }
        // Filter by category IDs
        if (categoryIds != null && !categoryIds.isEmpty()) {
            List<Project> filteredProjects = new ArrayList<>();
            for (Project project : projects) {
                if (categoryIds.contains(project.getProjectCategory().getId())) {
                    filteredProjects.add(project);
                }
            }
            projects = filteredProjects;
        }

        // Filter by skill IDs
                if (skillIds != null && !skillIds.isEmpty()) {
                    List<Project> filteredProjects = new ArrayList<>();
                    for (Project project : projects) {
                        for (Skill skill : project.getProjectSkill()) {
                            if (skillIds.contains(skill.getId())) {
                                filteredProjects.add(project);
                                break;
                            }
                        }
                    }
                    projects = filteredProjects;
                }

        // Filter by budget
        if (minBudget != null && maxBudget != null) {
            projects = projects.stream()
                    .filter(project -> project.getMinBudget() >= minBudget && project.getMaxBudget() <= maxBudget)
                    .collect(Collectors.toList());
        }

        // Filter by duration
        if (duration != null) {
            projects = projects.stream()
                    .filter(project -> project.getExpectedDuration() == duration)
                    .collect(Collectors.toList());
        }

        // Filter by status
        if (status != null) {
            projects = projects.stream()
                    .filter(project -> project.getStatus() == status)
                    .collect(Collectors.toList());
        }

        if (sortBy != null && sortDes != null) {
            projects = projects.stream()
                    .sorted((p1, p2) -> {
                        if (sortDes) {
                            return getSortValue(p2, sortBy).compareTo(getSortValue(p1, sortBy));
                        } else {
                            return getSortValue(p1, sortBy).compareTo(getSortValue(p2, sortBy));
                        }
                    })
                    .collect(Collectors.toList());
        }

        // Convert the projects to ProjectDetailsResponse objects

        return projectMapper.entityToDetailsResponse(projects);
    }

    private Comparable getSortValue(Project project, ProjectSortTypes sortBy) {
        switch (sortBy) {
            case CreateDate:
                return project.getCreateDate();
            case Duration:
                return project.getExpectedDuration();
            case Budget:
                return project.getMinBudget(); // or project.getMaxBudget() depending on your requirement
            case NoOfOffers:
                return project.getOfferCount();
            default:
                throw new UnsupportedOperationException("Unsupported sort type");
        }}

    public ProjectDetailsResponse get(Long id) {
        return projectMapper.entityToDetailsResponse(projectRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project Not Found")));
    }

    public List<ProjectDetailsResponse> getByUser(Integer id) {
        List<Project> projectsByUserId = projectRepository.findProjectsByUserIdOrOfferUserId(id);
        return projectMapper.entityToDetailsResponse(projectsByUserId);
    }

    public List<ProjectDetailsResponse> getByProfile(Long clientId, Long workerId) {
        if(workerId == null && clientId == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no profile id given (clientId or workerId)");
        }
        if(clientId != null){
            return projectMapper.entityToDetailsResponse(projectRepository.findAllByClient(ClientProfile.builder().id(clientId).build()));
        }
        return projectMapper.entityToDetailsResponse(projectRepository.findAllByWorker(WorkerProfile.builder().id(workerId).build()));
    }

    public ProjectDetailsResponse create(CreateProjectRequest createProjectRequest) {
        //TODO check wallet  : not needed because until itis a prroved i donot need the money
        Integer userId = auditAware.getCurrentAuditor().orElseThrow(() -> new RuntimeException("Auditor ID not found"));
        Project project = projectMapper.toEntity(createProjectRequest);
        project.setCreateDate(Date.from(Instant.now()));
        project.setStatus(ProjectStatus.open);
        project = projectRepository.save(project);
        return projectMapper.entityToDetailsResponse(project);
    }
    public ProjectDetailsResponse update(Long projectId, UpdateProjectRequest updateProjectRequest) throws ResponseStatusException{
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project Not Found"));
        projectMapper.updateFromDto(project, updateProjectRequest);
        projectRepository.save(project);
//        if(updateProjectRequest.projectCategoryId() != null){
//            project.setProjectCategory(updateProjectRequest.projectCategoryId());
//        }
//        if(updateProjectRequest.projectSkillIds() != null) {
//            project.setProjectSkill(updateProjectRequest.projectSkillIds().stream().map(id -> Skill.builder().id(id).build()).collect(Collectors.toSet()));
//        }

        Project project1 = projectRepository.findById(project.getId()).orElseThrow();
        return projectMapper.entityToDetailsResponse(project1);
    }

    public Map<String, String> delete(Long projectId) throws ResponseStatusException{
        Integer userId = auditAware.getCurrentAuditor().orElseThrow(() -> new RuntimeException("Auditor ID not found"));
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project does not exist"));
//        if(!projectRepository.existsByClient_UserId(userId)){
        if(!project.getClient().getUser().getId().equals(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Project does not belong to the user");
        }if(!List.of(ProjectStatus.open, ProjectStatus.closed).contains(project.getStatus())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot Delete project after it settled.");
        }
        projectRepository.deleteById(projectId);
        return Map.of("message", "Project Deleted");
    }

    public Map<String, String> close(Long projectId) throws ResponseStatusException{
        Integer userId = auditAware.getCurrentAuditor().orElseThrow(() -> new RuntimeException("Auditor ID not found"));
        if(!projectRepository.existsByClient_UserId(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Project does not belong to the user");
        }
        Integer closed = projectRepository.updateStatusByProjectIdAndStatus(ProjectStatus.closed, projectId, ProjectStatus.open);
        if(closed>0){
            return Map.of("message", "Project Closed");
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
    public void updateInternalFromOffer(Long offerId, ProjectStatus projectStatus, Long workerId) throws ResponseStatusException{
        Project project = projectRepository.findByOfferId(offerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project Not Found"));
        if(projectStatus != null){
            project.setStatus(projectStatus);
        }
        if(workerId != null){
            project.setWorker(WorkerProfile.builder().id(workerId).build());
        }
//        return projectRepository.save(project);
    }

    @Transactional
    public Map<String, String> submit(Long id) {
        Integer userId = auditAware.getCurrentAuditor().orElseThrow(() -> new RuntimeException("Auditor ID not found"));
//        if(!offerRepository.existsByWorker_UserId(userId)){
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Offer does not belong to the user");
//        }
        if(!offerRepository.existsByProjectIdAndStatusAndWorker_UserId(id, OfferStatus.accepted, userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Offer should be accepted when submitting");
        }
        updateInternal(id, ProjectStatus.submitted, null);
        return Map.of("message", "Project Submitted");
    }

    @Transactional
    public Map<String, String> complete(Long id) {
        Integer userId = auditAware.getCurrentAuditor().orElseThrow(() -> new RuntimeException("Auditor ID not found"));
        Project project = projectRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project Not Found"));
        if(!project.getClient().getUser().getId().equals(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Project does not belong to the user");
        }
        if(!project.getStatus().equals(ProjectStatus.submitted)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot complete project before it's submitted by the worker");
        }
        updateInternal(id, ProjectStatus.completed, null);
        return Map.of("message", "Project Completed");
    }
}
