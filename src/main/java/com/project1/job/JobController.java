package com.project1.job;

import com.project1.job.data.AddLikeToJobRequest;
import com.project1.job.data.JobDTO;
import com.project1.job.data.JobRequest;
import com.project1.profile.AddPhotoToClientProfileRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/jobs")
@AllArgsConstructor
public class JobController {

    private final JobService jobService;


    @GetMapping("/{workerProfileId}")
    public List<JobDTO>getJobById(@PathVariable Long workerProfileId) {
        List<JobDTO> jobs = jobService.getAllJobByWorkerProfileId(workerProfileId);
        return  jobs;
    }

    @PostMapping
    public ResponseEntity<JobDTO> createJob(@RequestBody JobRequest jobRequest) {
        JobDTO createdJob = jobService.createJob(jobRequest);
        return new ResponseEntity<>(createdJob, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobDTO> updateJob(@PathVariable Long id, @RequestBody JobDTO jobDTO) {
        JobDTO updatedJob = jobService.updateJob(id, jobDTO);
        return new ResponseEntity<>(updatedJob, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PreAuthorize("hasAnyRole('WORKER','CLIENT_WORKER','CLIENT','ADMIN')")
    @PostMapping("/add-like")
    public ResponseEntity<String> addLikeToJob(@RequestBody @Valid AddLikeToJobRequest request) {
        return jobService.addLikeToJob(request.getJobId());
    }
    @PreAuthorize("hasAnyRole('WORKER','CLIENT_WORKER','CLIENT','ADMIN')")
    @GetMapping("/is-liked")
    public ResponseEntity<Boolean> hasUserLikedJob(@RequestBody @Valid AddLikeToJobRequest request) {
        boolean hasLiked = jobService.hasUserLikedJob(request.getJobId());
        return ResponseEntity.ok(hasLiked);
    }

    @PreAuthorize("hasAnyRole('WORKER','CLIENT_WORKER','CLIENT','ADMIN')")
    @PostMapping("/add-view")
    public ResponseEntity<String> addViewToJob(@RequestBody @Valid AddLikeToJobRequest request) {
        return jobService.addViewToJob(request.getJobId());
    }


}