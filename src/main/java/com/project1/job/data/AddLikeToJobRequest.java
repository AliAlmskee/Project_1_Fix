package com.project1.job.data;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddLikeToJobRequest {
    @NotNull
    private Long jobId;
}
