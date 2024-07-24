package com.project1.offer;

import com.project1.auditing.ApplicationAuditAware;
import com.project1.offer.data.*;
import com.project1.profile.WorkerProfile;
import com.project1.project.ProjectRepository;
import com.project1.project.ProjectService;
import com.project1.project.data.Project;
import com.project1.project.data.ProjectStatus;
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

    public Map<String, String> delete(Long projectId) throws ResponseStatusException{
        Integer userId = auditAware.getCurrentAuditor().orElseThrow(() -> new RuntimeException("Auditor ID not found"));
        if(!offerRepository.existsByProject_Client_UserId(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Project does not belong to the user");
        }
        boolean deleted = offerRepository.deleteByIdAndStatus(projectId, OfferStatus.pending);
        if(deleted){
            return Map.of("message", "Offer Deleted");
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot Delete project after it settled.");
    }

    @Transactional
    public Map<String, String> accept(Long id) {
        //TODO hold from wallet
        Integer userId = auditAware.getCurrentAuditor().orElseThrow(() -> new RuntimeException("Auditor ID not found"));
        if(!offerRepository.existsByProject_Client_UserId(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Project does not belong to the user");
        }
        Offer offer = offerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Offer Not Found"));
        offer.setStatus(OfferStatus.accepted);
        projectService.updateInternalFromOffer(id, ProjectStatus.inProgress, offer.getWorker().getId());
        offerRepository.updateStatusOfSameProject(OfferStatus.dropped, id);
        return Map.of("message", "Offer Accepted");
    }
    public Map<String, String> reject(Long id) {
        Integer userId = auditAware.getCurrentAuditor().orElseThrow(() -> new RuntimeException("Auditor ID not found"));
        if(!offerRepository.existsByProject_Client_UserId(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Project does not belong to the user");
        }
        updateInternal(id, OfferStatus.accepted);
        return Map.of("message", "Offer Rejected");
    }

    @Transactional
    public Map<String, String> complete(Long id) {
        Integer userId = auditAware.getCurrentAuditor().orElseThrow(() -> new RuntimeException("Auditor ID not found"));
        if(!offerRepository.existsByProject_Client_UserId(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Project does not belong to the user");
        }
        //TODO transfer money + is there extra logic?
        projectService.updateInternalFromOffer(id, ProjectStatus.delivered, null);
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
