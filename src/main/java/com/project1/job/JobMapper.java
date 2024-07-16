package com.project1.job;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;


@Mapper(componentModel = "spring")
public interface JobMapper {

   // @Mapping(source = "viewsNo", target = "views")
   //@Mapping(source = "likeNo", target = "likes")
    JobDTO jobToJobDTO(Job job);

   // @Mapping(source = "views", target = "viewsNo")
  // @Mapping(source = "likes", target = "likeNo")
    Job jobDTOToJob(JobDTO jobDTO);

    List<JobDTO> jobsToJobDTOs(List<Job> jobs);

    List<Job> jobDTOsToJobs(List<JobDTO> jobDTOs);

}
