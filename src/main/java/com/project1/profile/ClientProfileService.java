package com.project1.profile;

import com.project1.auditing.ApplicationAuditAware;
import com.project1.fileSystem.Photo;
import com.project1.fileSystem.PhotoRepository;
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
public class ClientProfileService {

    private final ClientProfileRepository clientProfileRepository;
    private final PhotoRepository photoRepository;
    private final JobTitleRepository jobTitleRepository;
    private final ClientProfileMapper clientProfileMapper;
    private final ApplicationAuditAware auditAware;
    private final SkillRepository skillRepository;


    public List<ClientProfileDTO> findAllByUserId(Long userId) {
        List<ClientProfile> clientProfiles = clientProfileRepository.findAllByUserId(userId);
        return clientProfiles.stream()
                .map(clientProfileMapper::toDto)
                .collect(Collectors.toList());
    }

    public ResponseEntity<ClientProfileDTO> save(ClientProfileRequest clientProfileRequest) {
        User currentAuditor = auditAware.getCurrentUser().orElseThrow(() -> new RuntimeException("Auditor ID not found"));
        JobTitle jobTitle = jobTitleRepository.findById(clientProfileRequest.getJobTitleId()).orElseThrow(() -> new RuntimeException("jobTitle ID not found"));
       ClientProfile clientProfile = clientProfileMapper.toEntity(clientProfileRequest);
        clientProfile.setUser(currentAuditor);
        clientProfile.setRate(2.5);
        clientProfile.set_verified(false);
        clientProfile.setJobTitle(jobTitle);
        ClientProfile savedClientProfile = clientProfileRepository.save(clientProfile);
       return ResponseEntity.
               created(URI.create("/client-profiles/" + savedClientProfile.getId())).
               body(clientProfileMapper.toDto(savedClientProfile));
    }

    public ResponseEntity<ClientProfileDTO> updateById(Long id, ClientProfileRequest clientProfileRequest) {
      ClientProfile clientProfile = clientProfileRepository.findById(id)
              .orElseThrow(() -> new RuntimeException(" ID not found"));


        clientProfile.setBio(clientProfileRequest.getBio());
        if(clientProfileRequest.getJobTitleId()!=null){
        JobTitle jobTitle = jobTitleRepository.findById(clientProfileRequest.getJobTitleId()).orElseThrow();
        clientProfile.setJobTitle(jobTitle);
        }
        clientProfileRepository.save(clientProfile);
            return ResponseEntity.ok(clientProfileMapper.toDto(clientProfile));

    }


    public void deleteById(Long id) {
        clientProfileRepository.deleteById(id);

    }
    public ResponseEntity<String> addPhotoToClientProfile(Long clientProfileId, Long photoId) {
        ClientProfile clientProfile = clientProfileRepository.findById(clientProfileId).orElseThrow(() -> new RuntimeException("clientProfile ID not found"));
        Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new RuntimeException("Photo ID not found"));

        clientProfile.getPhotos().add(photo);
        clientProfileRepository.save(clientProfile);

        return ResponseEntity.ok("Photo added successfully");
    }


    public ResponseEntity<String> addSkillToClientProfile(Long clientProfileId, Long skillId) {
        ClientProfile clientProfile = clientProfileRepository.findById(clientProfileId).orElseThrow(() -> new RuntimeException("Client profile ID not found"));
        Skill skill = skillRepository.findById(skillId).orElseThrow(() -> new RuntimeException("Skill ID not found"));

        clientProfile.getSkills().add(skill);
        clientProfileRepository.save(clientProfile);

        return ResponseEntity.ok("Skill added successfully");
    }
    



}
