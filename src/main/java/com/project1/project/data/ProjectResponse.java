package com.project1.project.data;

import com.project1.profile.ClientProfileDTO;
import com.project1.profile.WorkerProfileDTO;
import com.project1.user.User;
import jakarta.annotation.Nullable;
import lombok.Builder;

import java.util.Date;

@Builder
public record ProjectResponse (
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
    WorkerProfileDTO worker
){}
