package com.project1.skill;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SkillResponse {
    private Long id;
    private String name;
    private Long categoryId;
    private String categoryName;
}
