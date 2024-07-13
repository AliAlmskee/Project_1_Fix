package com.project1.project.data;

import java.util.Date;
import java.util.Set;

public record CreateProjectRequest(
    String name,
    String description,
    Long minBudget,
    Long maxBudget,
    Long ExpectedDuration,
    ProjectStatus status,
    Date createDate,
    Integer clientId,
    Integer workerId,
    Set<Long> projectSkillIds,
    Set<Long> projectCategoriesIds
){}
