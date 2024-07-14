package com.project1.profile;

import lombok.Data;

@Data
public class AddSkillToWorkerProfileRequest {
    private Long workerProfileId;
    private Long skillId;
}