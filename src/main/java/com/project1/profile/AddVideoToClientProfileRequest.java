package com.project1.profile;

import lombok.Data;

@Data
public class AddVideoToClientProfileRequest {
    private Long clientProfileId;
    private Long videoId;
}
