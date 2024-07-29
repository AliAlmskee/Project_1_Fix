package com.project1.skill;

import com.project1.profile.ClientProfileDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/skill")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    @GetMapping("/{catId}")
    public ResponseEntity<Map<String, Object>> getAllSkills(@PathVariable("catId") Long catId) {
        ResponseEntity<List<SkillResponse>> skills = skillService.getAllSkills(catId);
        Map<String, Object> response = new HashMap<>();
        response.put("clientProfiles", skills);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<SkillResponse> createSkill(@RequestBody SkillRequest skillRequest) {
        return skillService.createSkill(skillRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSkill(@PathVariable Long id) {
        return skillService.deleteSkill(id);
    }
}
