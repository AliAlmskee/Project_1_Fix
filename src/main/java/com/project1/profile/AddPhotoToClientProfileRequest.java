package com.project1.profile;

import lombok.Data;

@Data
public class AddPhotoToClientProfileRequest {
    private Long clientProfileId;
    private Long photoId;
}
