package com.project1.project.data;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record CreateProjectRequest(
        @NotNull
        String name,
        @NotNull
        String description,
        @NotNull
        Long minBudget,
        @NotNull
        Long maxBudget,
        @NotNull
        Long ExpectedDuration,
        @NotNull
        Long clientProfileId,
        @NotNull
        Set<Long> projectSkillIds,
        @NotNull
        Long projectCategoryId
) {
}
