package com.project1.profile;
import lombok.Data;

@Data
public class AddPhotoToWorkerProfileRequest {
    private Long workerProfileId;
    private Long photoId;
}