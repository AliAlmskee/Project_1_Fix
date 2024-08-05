package com.project1.project.data;

import java.util.Set;

public record CreateProjectRequest(
    String name,
    String description,
    Long minBudget,
    Long maxBudget,
    Long ExpectedDuration,
    Long clientProfileId,
    Set<Long> projectSkillIds,
    Long projectCategoryId
){}
