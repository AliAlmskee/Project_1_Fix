package com.project1.profile;
import lombok.Data;

@Data
public class AddVideoToWorkerProfileRequest {
    private Long workerProfileId;
    private Long videoId;
}