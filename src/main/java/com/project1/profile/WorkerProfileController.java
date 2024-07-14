package com.project1.profile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/workerProfiles")
@RequiredArgsConstructor
public class WorkerProfileController {

    private final WorkerProfileService workerProfileService;

    @GetMapping("/{user_id}")
    public List<WorkerProfileDTO> getWorkerProfilesByUserId(@PathVariable Long user_id) {
        return workerProfileService.findAllByUserId(user_id);
    }

    @PostMapping
   // @PreAuthorize("hasAnyRole('WORKER','CLIENT_WORKER')")
    public ResponseEntity<WorkerProfileDTO> createWorkerProfile(@Valid @RequestBody WorkerProfileRequest workerProfile) {
        return workerProfileService.save(workerProfile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkerProfileDTO> updateWorkerProfile(@PathVariable Long id, @RequestBody WorkerProfileRequest workerProfile) {
        return workerProfileService.updateById(id, workerProfile);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkerProfile(@PathVariable Long id) {
        workerProfileService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/add-photo")
    public ResponseEntity<String> addPhotoToWorkerProfile(@RequestBody AddPhotoToWorkerProfileRequest request) {
        return workerProfileService.addPhotoToWorkerProfile(request.getWorkerProfileId(), request.getPhotoId());
    }

    @PostMapping("/add-skill")
    public ResponseEntity<String> addSkillToWorkerProfile(@RequestBody AddSkillToWorkerProfileRequest request) {
        return workerProfileService.addSkillToWorkerProfile(request.getWorkerProfileId(), request.getSkillId());
    }
}