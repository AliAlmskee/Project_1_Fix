package com.project1.jobTitle;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobTitleService {

    private final JobTitleRepository jobTitleRepository;

    public ResponseEntity<List<JobTitleResponse>> getAllJobTitles() {
        List<JobTitle> jobTitles = jobTitleRepository.findAll();
        List<JobTitleResponse> responses = jobTitles.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    public ResponseEntity<JobTitleResponse> createJobTitle(JobTitleRequest jobTitleRequest) {
        JobTitle jobTitle = new JobTitle();
        jobTitle.setTitle(jobTitleRequest.getTitle());
        jobTitleRepository.save(jobTitle);
        return ResponseEntity.ok(mapToResponse(jobTitle));
    }


    public void deleteJobTitle(Long id)
    {
        jobTitleRepository.deleteById(id);

    }

    private JobTitleResponse mapToResponse(JobTitle jobTitle) {
        return JobTitleResponse.builder()
                .id(jobTitle.getId())
                .title(jobTitle.getTitle())
                .build();
    }
}