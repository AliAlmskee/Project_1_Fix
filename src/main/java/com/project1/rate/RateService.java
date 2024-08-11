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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.time.Instant;
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
}
