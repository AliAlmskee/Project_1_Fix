package com.project1.offer;

import com.project1.auditing.ApplicationAuditAware;
import com.project1.offer.data.*;
import com.project1.profile.WorkerProfile;
import com.project1.project.ProjectRepository;
import com.project1.project.ProjectService;
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
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OfferService {
    private final OfferMapper offerMapper;
    private final OfferRepository offerRepository;
    private final ApplicationAuditAware auditAware;
    private final ProjectRepository projectRepository;
    private final ProjectProgressRepository projectProgressRepository;
    private final ProjectService projectService;


    public List<OfferResponse> getByProject(Long projectId) {
        return offerMapper.entityToResponse(offerRepository.findAllByProjectId(projectId));
    }

    public List<OfferResponse> getByWorker(Long id) {
        final WorkerProfile worker = WorkerProfile.builder().id(id).build();
        return offerMapper.entityToResponse(offerRepository.findAllByWorker(worker));
    }

    public OfferResponse create(CreateOfferRequest createOfferRequest) {
        Offer offer = offerMapper.toEntity(createOfferRequest);
        offer.setStatus(OfferStatus.pending);
        offer.setCreateDate(Date.from(Instant.now()));
        return offerMapper.entityToResponse(offerRepository.save(offer));
    }


    public OfferResponse update(Long offerId, UpdateOfferRequest updateOfferRequest) throws ResponseStatusException{
        Offer offer = offerRepository.findById(offerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Offer Not Found"));
        offerMapper.updateFromDto(offer, updateOfferRequest);
        return offerMapper.entityToResponse(offerRepository.save(offer));
    }

    public Map<String, String> delete(Long id) throws ResponseStatusException{
        Integer userId = auditAware.getCurrentAuditor().orElseThrow(() -> new RuntimeException("Auditor ID not found"));
        Offer offer = offerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Offer does not exist"));
        if(!offer.getWorker().getUser().getId().equals(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Offer does not belong to the user");
        }
        if(!List.of(OfferStatus.rejected, OfferStatus.pending, OfferStatus.dropped).contains(offer.getStatus())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot Delete Offer after it's accpeted.");
        }
        offerRepository.deleteById(id);
        return Map.of("message", "Offer Deleted");
    }

    @Transactional
    public Map<String, String> accept(Long id) {
        //TODO hold from wallet
        Integer userId = auditAware.getCurrentAuditor().orElseThrow(() -> new RuntimeException("Auditor ID not found"));
        if(!offerRepository.existsByProject_Client_UserId(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Project does not belong to the user");
        }
        Offer offer = offerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Offer Not Found"));
        if(!offer.getStatus().equals(OfferStatus.pending)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Offer should be pending when accepted");
        }
        ProjectProgress projectProgress = ProjectProgress.builder().acceptDate(Date.from(Instant.now())).build();
        offer.setProjectProgress(projectProgressRepository.save(projectProgress));
        offer.setStatus(OfferStatus.accepted);
        offerRepository.save(offer);
        projectService.updateInternalFromOffer(id, ProjectStatus.inProgress, offer.getWorker().getId());
        offerRepository.updateStatusOfSameProject(OfferStatus.dropped, id);
        return Map.of("message", "Offer Accepted");
    }
    public Map<String, String> reject(Long id) {
        Integer userId = auditAware.getCurrentAuditor().orElseThrow(() -> new RuntimeException("Auditor ID not found"));
        if(!offerRepository.existsByProject_Client_UserId(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Project does not belong to the user");
        }
        Offer offer = offerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Offer Not Found"));
        if(!offer.getStatus().equals(OfferStatus.pending)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Offer should be pending when rejected");
        }
        updateInternal(id, OfferStatus.rejected);
        return Map.of("message", "Offer Rejected");
    }

    @Transactional
    public Map<String, String> submit(Long id) {
        Integer userId = auditAware.getCurrentAuditor().orElseThrow(() -> new RuntimeException("Auditor ID not found"));
        if(!offerRepository.existsByWorker_UserId(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Offer does not belong to the user");
        }
        if(!offerRepository.existsByIdAndStatus(id, OfferStatus.accepted)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Offer should be accepted when submitting");
        }
        projectService.updateInternalFromOffer(id, ProjectStatus.submitted, null);
        return Map.of("message", "Project Submitted");
    }
    @Transactional
    public Map<String, String> complete(Long id) {
        Integer userId = auditAware.getCurrentAuditor().orElseThrow(() -> new RuntimeException("Auditor ID not found"));
        Offer offer = offerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Offer Not Found"));
        if(!offer.getProject().getClient().getUser().getId().equals(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Project does not belong to the user");
        }
        if(!offer.getProject().getStatus().equals(ProjectStatus.submitted)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot complete project before it's submitted by the worker");
        }
        projectService.updateInternalFromOffer(id, ProjectStatus.completed, null);
        return Map.of("message", "Project Completed");
    }

    //-------ADMIN--------
    public Map<String, String> adminDelete(Long offerId){
        offerRepository.deleteById(offerId);
        return Map.of("message", "Offer Deleted.");
    }


    //-------Internal--------

    //for updating status in progress
    public int updateInternal(Long offerId, OfferStatus offerStatus){
        return offerRepository.updateStatusById(offerStatus, offerId);
    }
//    public int updateProjectFromOffer(Long offerId, ProjectStatus status){
//        return projectRepository.updateStatusByOfferId(status, offerId);
//    }
}
