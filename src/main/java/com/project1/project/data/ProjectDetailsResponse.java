package com.project1.project.data;

import com.project1.category.Category;
import com.project1.category.CategoryDTO;
import com.project1.profile.ClientProfileDTO;
import com.project1.profile.WorkerProfile;
import com.project1.profile.WorkerProfileDTO;
import com.project1.skill.Skill;
import com.project1.skill.SkillDTO;
import com.project1.user.User;
import jakarta.annotation.Nullable;
import lombok.Builder;

import java.util.Date;
import java.util.Set;

@Builder
public record ProjectDetailsResponse(
    Long id,
    String name,
    String description,
    Long minBudget,
    Long maxBudget,
    Long ExpectedDuration,
    ProjectStatus status,
    Date createDate,
    ClientProfileDTO client,
    @Nullable
    WorkerProfileDTO worker,
    Set<CategoryDTO> projectCategories,
    Set<SkillDTO> projectSkill
){}
