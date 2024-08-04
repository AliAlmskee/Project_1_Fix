package com.project1.skill;

import com.project1.category.Category;
import com.project1.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;
    private final CategoryRepository categoryRepository;

    public ResponseEntity<List<SkillResponse>> getAllSkills(Long catId) {
        List<Skill> skills = skillRepository.findByCategoryId(catId);
        List<SkillResponse> skillResponses = skills.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(skillResponses);
    }

    public ResponseEntity<SkillResponse> createSkill(SkillRequest skillRequest) {
        Category category = categoryRepository.findById(skillRequest.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

        Skill skill = Skill.builder()
                .name(skillRequest.getName())
                .category(category)
                .build();

        Skill savedSkill = skillRepository.save(skill);
        return ResponseEntity.ok(mapToResponse(savedSkill));
    }

    public ResponseEntity<String> deleteSkill(Long id) {
        if (skillRepository.existsById(id)) {
            skillRepository.deleteById(id);
            return ResponseEntity.ok("Skill deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Skill not found");
        }
    }

    private SkillResponse mapToResponse(Skill skill) {
        return SkillResponse.builder()
                .id(skill.getId())
                .name(skill.getName())
                .categoryId(skill.getCategory().getId())
                .categoryName(skill.getCategory().getName())
                .build();
    }
}
