package com.project1.rate;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/rate")
@RequiredArgsConstructor
public class RateController {

    private final RateService rateService;

    @PostMapping
    private ResponseEntity<Map<String, String>> addProjectRate(@RequestBody  RateCreateDTO rateDTO, @RequestParam Long projectId){
        return ResponseEntity.ok(rateService.addRate(rateDTO, projectId));
    }

    @GetMapping("/profile/{profileId}")
    private ResponseEntity<ProfileRatesDTO> getProjectRates(@PathVariable("profileId") Long profileId, @RequestParam RatedType profileType){
        return ResponseEntity.ok(rateService.getProfilesRates(profileId, profileType));
    }

}
