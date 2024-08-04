package com.project1.job.data;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class JobRequest {
    private Long workerProfileId;
    private String name;
    private String description;
    private Date date;
    private List<Long> photoIds;
    private List<Long> skillIds;
}
