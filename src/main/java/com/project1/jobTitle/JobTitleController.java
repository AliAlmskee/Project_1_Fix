package com.project1.jobTitle;

import com.project1.category.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/job-titles")
public class JobTitleController {
    private final JobTitleService jobTitleService;

    @GetMapping
    public ResponseEntity<  Map<String, Object> > getAllJobTitles()
    {
        List<JobTitleResponse>  jobTitleResponses= jobTitleService.getAllJobTitles();
        Map<String, Object> response = new HashMap<>();
        response.put("jobTitles", jobTitleResponses);
        return ResponseEntity.ok(response);
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
