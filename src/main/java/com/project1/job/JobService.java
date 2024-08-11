package com.project1.job;


import com.project1.auditing.ApplicationAuditAware;
import com.project1.fileSystem.Photo;
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

import java.util.Iterator;
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
    public JobDTO getJobById(Long id) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        return jobOptional.map(jobMapper::jobToJobDTO).orElse(null);
    }
    @Transactional(readOnly = true)
    public List<JobDTO> getAllJobByWorkerProfileId(Long workerProfileId) {
        List<Job> jobs = jobRepository.findAllByWorkerProfileId(workerProfileId);
        return jobMapper.jobsToJobDTOs(jobs);
    }



    public JobDTO createJob(JobRequest jobRequest) {
        Job job = jobMapper.jobRequestToJob(jobRequest);
        Job createdJob = jobRepository.save(job);
        System.out.println(createdJob.getId());
        Optional<Job> savedJob =  jobRepository.findById(createdJob.getId());
        if(savedJob.isPresent()) {
            return jobMapper.jobToJobDTO(savedJob.get());
        }
        return null ;
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

        if (job.getLikedBy().stream().anyMatch(u -> u.getId().equals(user.getId()))) {
            throw new RuntimeException("User has already liked this job");
        }
        job.setLikeNo(job.getLikeNo() + 1 );
        job.getLikedBy().add(user);

        jobRepository.save(job);

        return ResponseEntity.ok("Like added successfully");
    }

    public ResponseEntity<String> deleteLikeToJob(Long jobId) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));
        User user = applicationAuditAware.getCurrentUser().orElseThrow(() -> new RuntimeException("User not found"));

        if (!job.getLikedBy().stream().anyMatch(u -> u.getId().equals(user.getId()))) {
            throw new RuntimeException("User has not liked this job");
        }

        job.setLikeNo(job.getLikeNo() - 1 );

        Iterator<User> iterator = job.getLikedBy().iterator();
        while (iterator.hasNext()) {
            User u = iterator.next();
            if (u.getId().equals(user.getId())) {
                iterator.remove();
                break;
            }
        }

        jobRepository.save(job);

        return ResponseEntity.ok("Like removed successfully");
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