package com.project1.job;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@AllArgsConstructor
public class JobController {

    private final JobService jobService;


    @GetMapping("/{workerProfileId}")
    public List<JobDTO>getJobById(@PathVariable Long workerProfileId) {
        List<JobDTO> jobs = jobService.getAllJobByWorkerProfileId(workerProfileId);
        return  jobs;
    }

    @PostMapping
    public ResponseEntity<JobDTO> createJob(@RequestBody JobDTO jobDTO) {
        JobDTO createdJob = jobService.createJob(jobDTO);
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

    @GetMapping("/{id}/views")
    public ResponseEntity<Integer> getJobViews(@PathVariable Long id) {
        int views = jobService.getJobViews(id);
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/{id}/likes")
    public ResponseEntity<Integer> getJobLikes(@PathVariable Long id) {
        int likes = jobService.getJobLikes(id);
        return new ResponseEntity<>(likes, HttpStatus.OK);
    }
}