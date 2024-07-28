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

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectMapper projectMapper;
    private final ProjectRepository projectRepository;
    private final ApplicationAuditAware auditAware;

    public List<ProjectWithOfferCountResponse> getAllFiltered(
            String search, List<Long> categories,
            List<Long> skills, Long minBudget,
            Long maxBudget, Long duration,
            ProjectStatus status, ProjectSortTypes sortBy,
            Boolean sortDes) {
        return projectMapper.entityWithOffersToResponse(projectRepository.findFilteredProjects(search, categories, skills, minBudget, maxBudget, duration, status, sortBy.name(), sortDes? "DESC" : "ASC"));
    }

    public List<ProjectResponse> getFilteredProjects(String namePattern, List<Long> categoryIds, List<Long> skillIds,
                                             Long minBudget, Long maxBudget, Long duration, ProjectStatus status,
                                             ProjectSortTypes sortBy, Boolean sortDes) {

//        StringBuilder queryBuilder = new StringBuilder("SELECT p.* " + "FROM Project p");
////                "(SELECT COUNT(*) FROM OrderOffer oo WHERE oo.order_id = p.id) AS offer_count " +
////                "FROM Project p " +
////                "LEFT JOIN ProjectCategory pc ON p.id = pc.project_id " +
////                "LEFT JOIN ProjectSkill ps ON p.id = ps.project_id " +
////                "WHERE 1=1 ");
//
//        if (namePattern != null && !namePattern.isEmpty()) {
//            queryBuilder.append("AND LOWER(p.name) LIKE LOWER(CONCAT('%', :namePattern, '%')) ");
//        }
//        if (categoryIds != null && !categoryIds.isEmpty()) {
//            queryBuilder.append("AND pc.category_id IN (:categoryIds) ");
//        }
//        if (skillIds != null && !skillIds.isEmpty()) {
//            queryBuilder.append("AND ps.skill_id IN (:skillIds) ");
//        }
//        if (minBudget != null && maxBudget != null) {
//            queryBuilder.append("AND ((p.minBudget BETWEEN :minBudget AND :maxBudget) " +
//                    "OR (p.maxBudget BETWEEN :minBudget AND :maxBudget) " +
//                    "OR (:minBudget BETWEEN p.minBudget AND p.maxBudget) " +
//                    "OR (:maxBudget BETWEEN p.minBudget AND p.maxBudget)) ");
//        }
//        if (duration != null) {
//            queryBuilder.append("AND p.ExpectedDuration <= :duration ");
//        }
//        if (status != null) {
//            queryBuilder.append("AND p.status = :status ");
//        }
//
//        String sortDirection = "DESC";
//        if (Objects.equals(sortDes, false)) {
//            sortDirection = "ASC";
//        }
//        if(sortBy != null){
//            queryBuilder.append("ORDER BY ");
//        }
//        if (ProjectSortTypes.NoOfOffers.equals(sortBy)) {
//            queryBuilder.append("offer_count ").append(sortDirection);
//        } else if(ProjectSortTypes.CreateDate.equals(sortBy)){
//            queryBuilder.append("p.").append("createDate").append(" ").append(sortDirection);
//        } else if(ProjectSortTypes.Budget.equals(sortBy)){
//            queryBuilder.append("p.").append("ExpectedDuration").append(" ").append(sortDirection);
//        } else if(ProjectSortTypes.Duration.equals(sortBy)){
//            queryBuilder.append("p.").append("minBudget").append(" ").append(sortDirection);
//        }
//
//        String query = queryBuilder.toString();
//
//        // Replace parameters in the query string
//        query = query.replace(":namePattern", namePattern != null ? namePattern : "")
//                .replace(":categoryIds", categoryIds != null ? categoryIds.stream().map(String::valueOf).collect(Collectors.joining(",")) : "")
//                .replace(":skillIds", skillIds != null ? skillIds.stream().map(String::valueOf).collect(Collectors.joining(",")) : "")
//                .replace(":minBudget", minBudget != null ? minBudget.toString() : "")
//                .replace(":maxBudget", maxBudget != null ? maxBudget.toString() : "")
//                .replace(":duration", duration != null ? duration.toString() : "")
//                .replace(":status", status != null ? "'" + status.name() + "'" : "");

        return projectMapper.entityToResponse(projectRepository.findAll());
    }

    public ProjectDetailsResponse get(Long id) {
        return projectMapper.entityToDetailsResponse(projectRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project Not Found")));
    }

    public List<ProjectResponse> getByUser(Integer id) {
        final User user = User.builder().id(id).build();
        return projectMapper.entityToResponse(projectRepository.findAllByClient_UserOrWorker_User(user, user));
    }
    public List<ProjectResponse> getByProfile(Long clientId, Long workerId) {
        if(workerId == null && clientId == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no profile id given (clientId or workerId)");
        }
        if(clientId != null){
            return projectMapper.entityToResponse(projectRepository.findAllByClient(ClientProfile.builder().id(clientId).build()));
        }
        return projectMapper.entityToResponse(projectRepository.findAllByWorker(WorkerProfile.builder().id(workerId).build()));
    }

    public ProjectResponse create(CreateProjectRequest createProjectRequest) {
        //TODO check wallet
        Integer userId = auditAware.getCurrentAuditor().orElseThrow(() -> new RuntimeException("Auditor ID not found"));
        Project project = projectMapper.toEntity(createProjectRequest);
        project.setCreateDate(Date.from(Instant.now()));
        project.setStatus(ProjectStatus.open);
         return projectMapper.entityToResponse(projectRepository.save(project));
    }
    public ProjectDetailsResponse update(Long projectId, UpdateProjectRequest updateProjectRequest) throws ResponseStatusException{
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project Not Found"));
        projectMapper.updateFromDto(project, updateProjectRequest);
        if(updateProjectRequest.projectCategoriesIds() != null){
            project.setProjectCategories(updateProjectRequest.projectCategoriesIds().stream().map(id -> Category.builder().id(id).build()).collect(Collectors.toSet()));
        }
        if(updateProjectRequest.projectSkillIds() != null) {
            project.setProjectSkill(updateProjectRequest.projectSkillIds().stream().map(id -> Skill.builder().id(id).build()).collect(Collectors.toSet()));
        }
        return projectMapper.entityToDetailsResponse(projectRepository.save(project));
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
}
