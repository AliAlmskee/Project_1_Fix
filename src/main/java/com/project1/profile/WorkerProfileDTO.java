package com.project1.profile;

import com.project1.category.Category;
import com.project1.category.CategoryDTO;
import com.project1.fileSystem.DocDTO;
import com.project1.fileSystem.PhotoDTO;
import com.project1.fileSystem.VideoDTO;
import com.project1.jobTitle.JobTitleDTO;
import com.project1.skill.SkillDTO;
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
public class WorkerProfileDTO {
    private Long id;

    private String bio;
    private int userId;
    private double rate;
    private JobTitleDTO jobTitleDTO;
    private CategoryDTO categoryDTO;
    private List<PhotoDTO> photoDTOs;
    private List<VideoDTO> videoDTOs;
    private List<DocDTO> docDTOs;
    private List<SkillDTO> skillDTOs;

    private UserDTO userDTO;

}