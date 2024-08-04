package com.project1.project;

import com.project1.category.Category;
import com.project1.project.data.*;
import com.project1.skill.Skill;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllFiltered(
            @RequestParam @Nullable String search,
            @RequestParam @Nullable List<Long> categories,
            @RequestParam @Nullable List<Long> skills,
            @RequestParam @Nullable Long minBudget,
            @RequestParam @Nullable Long maxBudget,
            @RequestParam @Nullable Long duration,
            @RequestParam @Nullable ProjectStatus status,
            @RequestParam @Nullable ProjectSortTypes sortBy,
            @RequestParam @Nullable Boolean sortDes
            ) {
        return ResponseEntity.ok(projectService.getFilteredProjects(search, categories, skills, minBudget, maxBudget, duration, status, sortBy, sortDes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDetailsResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.get(id));
    }

    @GetMapping("/byUser/{id}")
    public ResponseEntity<List<ProjectResponse>> getByUser(@PathVariable Integer id) {
        return ResponseEntity.ok(projectService.getByUser(id));
    }

    @GetMapping("/byProfile")
    public ResponseEntity<List<ProjectResponse>> getByProfile(@RequestParam(required = false) Long clientId, @RequestParam(required = false) Long workerId) {
        return ResponseEntity.ok(projectService.getByProfile(clientId, workerId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENT','CLIENT_WORKER')")
    public ResponseEntity<ProjectResponse> create(@RequestBody CreateProjectRequest createProjectRequest) {
        return ResponseEntity.ok(projectService.create(createProjectRequest));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT','CLIENT_WORKER')")
    public ResponseEntity<ProjectDetailsResponse> update(@PathVariable Long id, @RequestBody UpdateProjectRequest updateProjectRequest) throws ResponseStatusException {
        return  ResponseEntity.ok(projectService.update(id, updateProjectRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT','CLIENT_WORKER')")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) throws ResponseStatusException{
        return  ResponseEntity.ok(projectService.delete(id));
    }
    @PostMapping("/close/{id}")
    @PreAuthorize("hasAnyRole('CLIENT','CLIENT_WORKER')")
    public ResponseEntity<Map<String, String>> close(@PathVariable Long id) throws ResponseStatusException{
        return  ResponseEntity.ok(projectService.close(id));
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> adminDelete(@PathVariable Long id) throws ResponseStatusException{
        return  ResponseEntity.ok(projectService.adminDelete(id));
    }
}
