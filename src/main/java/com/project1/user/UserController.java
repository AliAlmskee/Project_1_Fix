package com.project1.user;

import com.project1.project.ProjectMapper;
import com.project1.project.data.Project;
import com.project1.project.data.ProjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final UserMapper userMapper;
    private final ProjectMapper projectMapper;
    @PatchMapping
    public ResponseEntity<?> changePassword(
          @RequestBody ChangePasswordRequest request,
          Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

//
    @PostMapping("/{userId}/projects/{projectId}")
    public ResponseEntity<Void> addUserProject(@PathVariable int userId, @PathVariable Long projectId) {
        service.addUserProject(userId, projectId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/favorites/{favoriteUserId}")
    public ResponseEntity<Void> addFavoriteUser(@PathVariable int userId, @PathVariable int favoriteUserId) {
        service.addFavoriteUser(userId, favoriteUserId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/projects/{projectId}")
    public ResponseEntity<Void> removeUserProject(@PathVariable int userId, @PathVariable Long projectId) {
        service.removeUserProject(userId, projectId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/favorites/{favoriteUserId}")
    public ResponseEntity<Void> removeFavoriteUser(@PathVariable int userId, @PathVariable int favoriteUserId) {
        service.removeFavoriteUser(userId, favoriteUserId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/favorites")
    public ResponseEntity<Map<String, Object>> getFavoriteUserIds(@PathVariable int userId) {
        List<User> favoriteUsers = service.getFavoriteUser(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("favoriteUsers", userMapper.usersToUserDTOs(favoriteUsers));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/projects")
    public ResponseEntity<Map<String, Object>> getFavoriteProjectIds(@PathVariable int userId) {
        List<Project> favoriteProjects = service.getFavoriteProject(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("favoriteProjects", projectMapper.projectsToProjectResponses(favoriteProjects));
        return ResponseEntity.ok(response);
    }


}
