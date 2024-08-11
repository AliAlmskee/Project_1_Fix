package com.project1.job.data;

import com.project1.fileSystem.PhotoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobDTO {
    private Long id;
    private Long workerProfileId;
    private String name;
    private String description;
    private int viewsNo;
    private int likeNo;
    private Date date;
    private List<PhotoDTO> photoDTOs;
    private List<Long> skillIds;
}
