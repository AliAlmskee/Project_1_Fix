package com.project1.job;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;



    @Transactional(readOnly = true)
    public List<JobDTO> getAllJobByWorkerProfileId(Long workerProfileId) {
        List<Job> jobs = jobRepository.findAllByWorkerProfileId(workerProfileId);
        return jobMapper.jobsToJobDTOs(jobs);
    }

    @Transactional(readOnly = true)
    public JobDTO getJobById(Long id) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        return jobOptional.map(jobMapper::jobToJobDTO).orElse(null);
    }

    @Transactional
    public JobDTO createJob(JobDTO jobDTO) {
        Job job = jobMapper.jobDTOToJob(jobDTO);
        Job createdJob = jobRepository.save(job);
        return jobMapper.jobToJobDTO(createdJob);
    }

    @Transactional
    public JobDTO updateJob(Long id, JobDTO jobDTO) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            job.setName(jobDTO.getName());
            job.setDescription(jobDTO.getDescription());
            // Update other fields as needed
            Job updatedJob = jobRepository.save(job);
            return jobMapper.jobToJobDTO(updatedJob);
        } else {
            return null;
        }
    }

    @Transactional
    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public int getJobViews(Long id) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        return jobOptional.map(Job::getViewsNo).orElse(0);
    }

    @Transactional(readOnly = true)
    public int getJobLikes(Long id) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        return jobOptional.map(Job::getLikeNo).orElse(0);
    }
}