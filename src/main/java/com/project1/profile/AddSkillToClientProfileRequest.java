package com.project1.profile;

import lombok.Data;

@Data
public class AddSkillToClientProfileRequest {
    private Long clientProfileId;
    private Long skillId;
}
