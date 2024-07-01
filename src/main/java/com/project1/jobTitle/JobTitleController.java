package com.project1.jobTitle;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/job-titles")
public class JobTitleController {
    private final JobTitleService jobTitleService;

    @GetMapping
    public ResponseEntity<List<JobTitleResponse>> getAllJobTitles()
    {
        return jobTitleService.getAllJobTitles();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<JobTitleResponse> createJobTitle(@RequestBody JobTitleRequest jobTitleRequest) {
        return jobTitleService.createJobTitle(jobTitleRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobTitle(@PathVariable Long id) {
        jobTitleService.deleteJobTitle(id);
        return ResponseEntity.noContent().build();
    }


}
