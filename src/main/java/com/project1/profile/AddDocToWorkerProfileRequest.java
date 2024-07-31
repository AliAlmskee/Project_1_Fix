package com.project1.profile;
import lombok.Data;

@Data
public class AddDocToWorkerProfileRequest {
    private Long workerProfileId;
    private Long docId;
}