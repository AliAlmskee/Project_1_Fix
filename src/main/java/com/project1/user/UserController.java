package com.project1.user;

import com.project1.project.ProjectMapper;
import com.project1.project.data.Project;
import com.project1.project.data.ProjectDetailsResponse;
import com.project1.project.data.ProjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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


    @PostMapping("/{userId}/projects/{projectId}")
    public ResponseEntity<Map<String, Object>> addUserProject(@PathVariable int userId, @PathVariable Long projectId) {
        service.addUserProject(userId, projectId);
        return ResponseEntity.ok(Map.of("message", "Project Added"));
    }

    @PostMapping("/{userId}/favorites/{favoriteUserId}")
    public ResponseEntity<Map<String, Object>> addFavoriteUser(@PathVariable int userId, @PathVariable int favoriteUserId) {
        service.addFavoriteUser(userId, favoriteUserId);
        return ResponseEntity.ok(Map.of("message", "User Added"));
    }

    @DeleteMapping("/{userId}/projects/{projectId}")
    public ResponseEntity<Map<String, Object>> removeUserProject(@PathVariable int userId, @PathVariable Long projectId) {
        service.removeUserProject(userId, projectId);
        return ResponseEntity.ok(Map.of("message", "Project Removed"));
    }

    @DeleteMapping("/{userId}/favorites/{favoriteUserId}")
    public ResponseEntity<Map<String, Object>> removeFavoriteUser(@PathVariable int userId, @PathVariable int favoriteUserId) {
        service.removeFavoriteUser(userId, favoriteUserId);
        return ResponseEntity.ok(Map.of("message", "User Removed"));
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
        response.put("favoriteProjects", projectMapper.entityToDetailsResponse(favoriteProjects));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<UserDTO> getUserByPhone(@PathVariable String phone) {
            UserDTO userDTO = service.getUserByPhone(phone);
            return ResponseEntity.ok(userDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/set_status")
    public ResponseEntity<UserDTO> getUserByPhone(@RequestBody UpdateStatusRequest updateStatusRequest) {
        UserDTO userDTO = service.updateStatusRequest(updateStatusRequest);
        return ResponseEntity.ok(userDTO);
    }

}
