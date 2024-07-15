package com.project1.project;

import com.project1.category.Category;
import com.project1.project.data.*;
import com.project1.skill.Skill;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
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
            @RequestParam @Nullable List<Category> categories,
            @RequestParam @Nullable List<Skill> skills,
            @RequestParam @Nullable Long minBudget,
            @RequestParam @Nullable Long maxBudget,
            @RequestParam @Nullable Long duration,
            @RequestParam @Nullable ProjectSortTypes sortBy,
            @RequestParam @Nullable Boolean sortDes
            ) {
        return ResponseEntity.ok(projectService.getAllFiltered(search, categories, skills, minBudget, maxBudget, duration, sortBy, sortDes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDetailsResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.get(id));
    }

    @GetMapping("/byUser/{id}")
    public ResponseEntity<List<ProjectResponse>> getByUser(@PathVariable Integer id) {
        return ResponseEntity.ok(projectService.getByUser(id));
    }


    @PostMapping
    public ResponseEntity<ProjectDetailsResponse> create(@RequestBody CreateProjectRequest createProjectRequest) {
        return ResponseEntity.ok(projectService.create(createProjectRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDetailsResponse> update(@PathVariable Long projectId, @RequestBody UpdateProjectRequest updateProjectRequest) throws ResponseStatusException {
        return  ResponseEntity.ok(projectService.update(projectId, updateProjectRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long projectId) throws ResponseStatusException{
        return  ResponseEntity.ok(projectService.delete(projectId));
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> adminDelete(@PathVariable Long projectId) throws ResponseStatusException{
        return  ResponseEntity.ok(projectService.adminDelete(projectId));
    }
}
