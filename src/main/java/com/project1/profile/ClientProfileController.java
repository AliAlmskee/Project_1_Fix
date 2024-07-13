package com.project1.profile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientProfiles")
@RequiredArgsConstructor
public class ClientProfileController {


    private final  ClientProfileService clientProfileService;

    @GetMapping("/{user_id}")
    public List<ClientProfileDTO> getClientProfilesByUserId(@PathVariable Long user_id) {
       return clientProfileService.findAllByUserId(user_id);
    }
   // @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT', 'WORKER', 'CLIENT_WORKER')")
    @PostMapping
    public ResponseEntity<ClientProfileDTO> createClientProfile(@Valid @RequestBody  ClientProfileRequest clientProfile) {

        return clientProfileService.save(clientProfile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientProfileDTO> updateClientProfile(@PathVariable Long id, @RequestBody ClientProfileRequest clientProfile) {
      return clientProfileService.updateById(id, clientProfile);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClientProfile(@PathVariable Long id) {
            clientProfileService.deleteById(id);
            return ResponseEntity.noContent().build();

    }



    @PostMapping("/add-photo")
    public ResponseEntity<String> addPhotoToClientProfile(@RequestBody AddPhotoToClientProfileRequest request) {
        return clientProfileService.addPhotoToClientProfile(request.getClientProfileId(), request.getPhotoId());
    }
    @PostMapping("/add-skill")
    public ResponseEntity<String> addSkillToClientProfile(@RequestBody AddSkillToClientProfileRequest request) {
        return clientProfileService.addSkillToClientProfile(request.getClientProfileId(), request.getSkillId());
    }


}
