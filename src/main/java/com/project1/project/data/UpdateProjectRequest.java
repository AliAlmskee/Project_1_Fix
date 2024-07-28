package com.project1.project.data;

import java.util.Date;
import java.util.Set;

public record UpdateProjectRequest(
    String name,
    String description,
    Long minBudget,
    Long maxBudget,
    Long ExpectedDuration,
    Set<Long> projectSkillIds,
    Set<Long> projectCategoriesIds
){}
