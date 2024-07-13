package com.project1.profile;

import com.project1.fileSystem.PhotoDTO;
import com.project1.jobTitle.JobTitleDTO;
import com.project1.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientProfileRequest {
    private String bio;
    private Long jobTitleId;
}
