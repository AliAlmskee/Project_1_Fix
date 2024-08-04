package com.project1.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkerProfileRequest {

    @NotBlank
    @NotNull
    private String bio;

    @NotNull
    private Long jobTitleId;

    @NotNull
    private Long categoryId;
}