package com.project1.job;


import com.project1.auditing.ApplicationAuditAware;
import com.project1.job.data.Job;
import com.project1.job.data.JobDTO;
import com.project1.job.data.JobRequest;
import com.project1.user.User;
import com.project1.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;
    private final UserRepository userRepository;
    private final ApplicationAuditAware applicationAuditAware;

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
    public JobDTO createJob(JobRequest jobDTO) {
        Job job = jobMapper.jobRequestToJob(jobDTO);
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
            job.setViewsNo(jobDTO.getViewsNo());
            job.setLikeNo(jobDTO.getLikeNo());
            job.setDate(jobDTO.getDate());

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


    public ResponseEntity<String> addLikeToJob(Long jobId) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));
        User user = applicationAuditAware.getCurrentUser().orElseThrow(() -> new RuntimeException("User not found"));

        if (job.getLikedBy().contains(user)) {
            throw new RuntimeException("User has already liked this job");
        }
        job.setLikeNo(job.getLikeNo() + 1 );
        job.getLikedBy().add(user);

        jobRepository.save(job);

        return ResponseEntity.ok("Like added successfully");
    }

    public boolean hasUserLikedJob(Long jobId) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));
        User user = applicationAuditAware.getCurrentUser().orElseThrow(() -> new RuntimeException("User not found"));

        return job.getLikedBy().stream()
                .anyMatch(likedUser -> likedUser.getId().equals(user.getId()));
    }


    public ResponseEntity<String> addViewToJob(Long jobId) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));
        User user = applicationAuditAware.getCurrentUser().orElseThrow(() -> new RuntimeException("User not found"));

        if (job.getViewedBy().contains(user)) {
            throw new RuntimeException("User has already Viewed this job");
        }
        job.setViewsNo(job.getViewsNo() + 1 );
        job.getViewedBy().add(user);

        jobRepository.save(job);

        return ResponseEntity.ok("View added successfully");
    }






}