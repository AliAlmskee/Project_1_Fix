package com.project1.job;

import com.project1.fileSystem.Photo;
import com.project1.job.data.Job;
import com.project1.job.data.JobDTO;
import com.project1.job.data.JobRequest;
import com.project1.profile.WorkerProfile;
import com.project1.skill.Skill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface JobMapper {

    @Mapping(source = "photos", target = "photoIds", qualifiedByName = "photosToIds")
    @Mapping(source = "skills", target = "skillIds", qualifiedByName = "skillsToIds")
    @Mapping(source = "workerProfile", target = "workerProfileId", qualifiedByName = "workerProfileToId")
    JobDTO jobToJobDTO(Job job);

    Job jobDTOToJob(JobDTO jobDTO);

    @Mapping(source = "workerProfileId", target = "workerProfile", qualifiedByName = "idToWorkerProfile")
    @Mapping(source = "photoIds", target = "photos", qualifiedByName = "idsToPhotos")
    @Mapping(source = "skillIds", target = "skills", qualifiedByName = "idsToSkills")
    Job jobRequestToJob(JobRequest jobRequest);

    List<JobDTO> jobsToJobDTOs(List<Job> jobs);

    List<Job> jobDTOsToJobs(List<JobDTO> jobDTOs);

    @Named("idToWorkerProfile")
    default WorkerProfile idToWorkerProfile(Long id){
        return WorkerProfile.builder().id(id).build();
    }

    @Named("photosToIds")
    default List<Long> photosToIds(List<Photo> photos){
        return photos.stream()
                .map(Photo::getId)
                .collect(Collectors.toList());
    }

    @Named("skillsToIds")
    default List<Long> skillsToIds(List<Skill> skills){
        return skills.stream()
                .map(Skill::getId)
                .collect(Collectors.toList());
    }

    @Named("idsToPhotos")
    default List<Photo> idsToPhotos(List<Long> photoIds){
        return photoIds.stream()
                .map(id -> Photo.builder().id(id).build())
                .collect(Collectors.toList());
    }

    @Named("idsToSkills")
    default List<Skill> idsToSkills(List<Long> skillIds){
        return skillIds.stream()
                .map(id -> Skill.builder().id(id).build())
                .collect(Collectors.toList());
    }

    @Named("workerProfileToId")
    default Long workerProfileToId(WorkerProfile workerProfile) {
        return workerProfile!= null? workerProfile.getId() : null;
    }
}