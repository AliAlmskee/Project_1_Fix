package com.project1.profile;


import com.project1.auditing.ApplicationAuditAware;
import com.project1.category.Category;
import com.project1.category.CategoryRepository;
import com.project1.fileSystem.*;
import com.project1.jobTitle.JobTitle;
import com.project1.jobTitle.JobTitleRepository;
import com.project1.skill.Skill;
import com.project1.skill.SkillRepository;
import com.project1.user.User;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WorkerProfileService {

    private final WorkerProfileRepository workerProfileRepository;
    private final PhotoRepository photoRepository;
    private final VideoRepository videoRepository;
    private final DocRepository docRepository;
    private final JobTitleRepository jobTitleRepository;
    private final WorkerProfileMapper workerProfileMapper;
    private final ApplicationAuditAware auditAware;
    private final SkillRepository skillRepository;
    private final CategoryRepository categoryRepository;

    public List<WorkerProfileDTO> findAllByUserId(Long userId) {
        List<WorkerProfile> workerProfiles = workerProfileRepository.findAllByUserId(userId);
        return workerProfiles.stream()
                .map(workerProfileMapper::toDto)
                .collect(Collectors.toList());
    }

    public ResponseEntity<WorkerProfileDTO> save(WorkerProfileRequest workerProfileRequest) {
        User currentAuditor = auditAware.getCurrentUser().orElseThrow(() -> new RuntimeException("Auditor ID not found"));
        JobTitle jobTitle = jobTitleRepository.findById(workerProfileRequest.getJobTitleId()).orElseThrow(() -> new RuntimeException("jobTitle ID not found"));
        Category category = categoryRepository.findById(workerProfileRequest.getCategoryId()).orElseThrow(() -> new RuntimeException("jobTitle ID not found"));
        WorkerProfile workerProfile = workerProfileMapper.toEntity(workerProfileRequest);
        workerProfile.setUser(currentAuditor);
        workerProfile.setRate(2.5);
        workerProfile.set_verified(false);
        workerProfile.setJobTitle(jobTitle);
        workerProfile.setCategory(category);
        WorkerProfile savedWorkerProfile = workerProfileRepository.save(workerProfile);
        return ResponseEntity.
                created(URI.create("/worker-profiles/" + savedWorkerProfile.getId())).
                body(workerProfileMapper.toDto(savedWorkerProfile));
    }

    public ResponseEntity<WorkerProfileDTO> updateById(Long id, WorkerProfileRequest workerProfileRequest) {
        WorkerProfile workerProfile = workerProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(" ID not found"));

        workerProfile.setBio(workerProfileRequest.getBio());
        if(workerProfileRequest.getJobTitleId()!=null){
            JobTitle jobTitle = jobTitleRepository.findById(workerProfileRequest.getJobTitleId()).orElseThrow();
            workerProfile.setJobTitle(jobTitle);
        }
        if(workerProfileRequest.getCategoryId()!=null){
            Category category = categoryRepository.findById(workerProfileRequest.getCategoryId()).orElseThrow(() -> new RuntimeException("jobTitle ID not found"));
            workerProfile.setCategory(category);
        }
        workerProfileRepository.save(workerProfile);
        return ResponseEntity.ok(workerProfileMapper.toDto(workerProfile));

    }


    public void deleteById(Long id) {
        workerProfileRepository.deleteById(id);

    }
    public ResponseEntity<String> addPhotoToWorkerProfile(Long workerProfileId, Long photoId) {
        WorkerProfile workerProfile = workerProfileRepository.findById(workerProfileId).orElseThrow(() -> new RuntimeException("workerProfile ID not found"));
        Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new RuntimeException("Photo ID not found"));

        workerProfile.getPhotos().add(photo);
        workerProfileRepository.save(workerProfile);

        return ResponseEntity.ok("Photo added successfully");
    }

    public ResponseEntity<String> addVideoToWorkerProfile(Long workerProfileId, Long videoId) {
        WorkerProfile workerProfile = workerProfileRepository.findById(workerProfileId).orElseThrow(() -> new RuntimeException("workerProfile ID not found"));
        Video video = videoRepository.findById(videoId).orElseThrow(() -> new RuntimeException("Video ID not found"));

        workerProfile.getVideos().add(video);
        workerProfileRepository.save(workerProfile);

        return ResponseEntity.ok("Video added successfully");
    }

    public ResponseEntity<String> addDocToWorkerProfile(Long workerProfileId, Long docId) {
        WorkerProfile workerProfile = workerProfileRepository.findById(workerProfileId).orElseThrow(() -> new RuntimeException("workerProfile ID not found"));
        Doc doc = docRepository.findById(docId).orElseThrow(() -> new RuntimeException("Doc ID not found"));

        workerProfile.getDocs().add(doc);
        workerProfileRepository.save(workerProfile);

        return ResponseEntity.ok("Doc added successfully");
    }


    public ResponseEntity<String> addSkillToWorkerProfile(Long workerProfileId, Long skillId) {
        WorkerProfile workerProfile = workerProfileRepository.findById(workerProfileId).orElseThrow(() -> new RuntimeException("Worker profile ID not found"));
        Skill skill = skillRepository.findById(skillId).orElseThrow(() -> new RuntimeException("Skill ID not found"));

        workerProfile.getSkills().add(skill);
        workerProfileRepository.save(workerProfile);

        return ResponseEntity.ok("Skill added successfully");
    }
}