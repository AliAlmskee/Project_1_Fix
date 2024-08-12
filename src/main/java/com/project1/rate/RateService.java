package com.project1.rate;

import com.project1.profile.ClientProfile;
import com.project1.profile.ClientProfileRepository;
import com.project1.profile.WorkerProfile;
import com.project1.profile.WorkerProfileRepository;
import com.project1.project.ProjectRepository;
import com.project1.project.data.ProjectStatus;
import com.project1.projectProgress.ProjectProgress;
import com.project1.projectProgress.ProjectProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RateService {
    private final RateRepository rateRepository;
    private final RateMapper rateMapper;
    private final ClientProfileRepository clientProfileRepository;
    private final WorkerProfileRepository workerProfileRepository;
    private final ProjectProgressRepository projectProgressRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public Map<String, String> addRate(RateCreateDTO rateDTO, Long projectId) {
        ProjectProgress projectProgress = projectProgressRepository.findByOffer_ProjectId(projectId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project Not Found"));
        boolean statusIsCompleted = projectRepository.existsByIdAndStatus(projectId, ProjectStatus.completed);
        if(!statusIsCompleted){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project should be completed to be rated");
        }
        Rate rate = rateMapper.createEntity(rateDTO);
        rate.setCreateDate(Date.from(Instant.now()));
        rateRepository.save(rate);
        if(rateDTO.rated().equals(RatedType.Client)){
            if(projectProgress.getClientRate() != null){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client Already Rated");
            }
            ClientProfile clientProfile = clientProfileRepository.findById(rateDTO.clientProfileId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client Profile Not Found!"));
            int clientCount = rateRepository.countByClient(clientProfile);
            clientProfile.addRate(rateDTO.totalRate(), clientCount);
            clientProfileRepository.save(clientProfile);
            projectProgress.setClientRate(rate);
            projectProgressRepository.save(projectProgress);
        }else{
            if(projectProgress.getWorkerRate() != null){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Worker Already Rated");
            }
            rateRepository.save(rate);
            WorkerProfile workerProfile = workerProfileRepository.findById(rateDTO.workerProfileId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Worker Profile Not Found!"));
            int workerCount = rateRepository.countByWorker(workerProfile);
            workerProfile.addRate(rateDTO.totalRate(), workerCount);
            workerProfileRepository.save(workerProfile);
            projectProgress.setWorkerRate(rate);
            projectProgressRepository.save(projectProgress);
        }
        return Map.of("message", "Rate Recorded");
    }

    public ProfileRatesDTO getProfilesRates(Long profileId, RatedType profileType) {
        List<Rate> rates = List.of();
        Double sum = 0.0;
        if(profileType == RatedType.Client){
            rates = rateRepository.findByClient_IdAndRated(profileId, RatedType.Client);
        }else{
            rates = rateRepository.findByWorker_IdAndRated(profileId, RatedType.Worker);
        }
        Integer[] counts = {0,0,0,0,0};
        for (Rate rate: rates) {
            Double totalRate = rate.totalRate();
            counts[rate.totalRate().intValue()]++;
            sum+= totalRate;
        }
        Integer count = rates.size();
        return ProfileRatesDTO.builder()
                .count(count)
                .avg(sum/count)
                .rates(rateMapper.EntityToDto(rates))
                .count1(counts[0])
                .count2(counts[1])
                .count3(counts[2])
                .count4(counts[3])
                .count5(counts[4])
                .build();
    }
}
