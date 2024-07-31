package com.project1.profile;

import lombok.Data;

@Data
public class AddDocToClientProfileRequest {
    private Long clientProfileId;
    private Long docId;
}
