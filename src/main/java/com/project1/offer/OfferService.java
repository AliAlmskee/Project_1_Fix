package com.project1.offer;

import com.google.firebase.internal.FirebaseService;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.project1.auditing.ApplicationAuditAware;
import com.project1.firebase.FCMService;
import com.project1.offer.data.*;
import com.project1.profile.WorkerProfile;
import com.project1.project.ProjectRepository;
import com.project1.project.ProjectService;
import com.project1.project.data.ProjectStatus;
import com.project1.transaction.TransactionService;
import com.project1.projectProgress.ProjectProgress;
import com.project1.projectProgress.ProjectProgressRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
    private final TransactionService transactionService;
    private final FCMService fcmService;
    @PersistenceContext
    private EntityManager entityManager;
    public List<OfferResponse> getByProject(Long projectId) {
        return offerMapper.entityToResponse(offerRepository.findAllByProjectId(projectId));
    }

    public List<OfferResponse> getByWorker(Long id) {
        final WorkerProfile worker = WorkerProfile.builder().id(id).build();
        return offerMapper.entityToResponse(offerRepository.findAllByWorker(worker));
    }

    @Transactional
    public OfferResponse create(CreateOfferRequest createOfferRequest) throws FirebaseMessagingException {
        Offer offer = offerMapper.toEntity(createOfferRequest);
        offer.setStatus(OfferStatus.pending);
        offer.setCreateDate(Date.from(Instant.now()));
        offer = offerRepository.saveAndFlush(offer);
        Offer offer1 = offerRepository.findById(offer.getId()).orElseThrow();
        entityManager.refresh(offer);
        fcmService.sendNotification("New Offer", "A new offer was made on your project!", offer.getProject().getClient().getUser().getDevice_token());
        return offerMapper.entityToResponse(offer);
    }


    public OfferResponse update(Long offerId, UpdateOfferRequest updateOfferRequest) throws ResponseStatusException{
        Offer offer = offerRepository.findById(offerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Offer Not Found"));
        offerMapper.updateFromDto(offer, updateOfferRequest);

        offer = offerRepository.save(offer);
        Offer offer1 = offerRepository.findById(offer.getId()).orElseThrow();
        return offerMapper.entityToResponse(offer1);
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
    public Map<String, String> accept(Long id) throws FirebaseMessagingException {
        transactionService.AcceptOfferTransaction(id);
        //TODO hold from wallet


        Integer userId = auditAware.getCurrentAuditor().orElseThrow(() -> new RuntimeException("Auditor ID not found"));
        if(!offerRepository.existsByProject_Client_UserId(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Project does not belong to the user");
        }
        Offer offer = offerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Offer Not Found"));
        if(!offer.getStatus().equals(OfferStatus.pending)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Offer should be pending when accepted");
        }if(!offer.getProject().getStatus().equals(ProjectStatus.open)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Project should be open when an offer accepted");
        }
        ProjectProgress projectProgress = ProjectProgress.builder().acceptDate(Date.from(Instant.now())).offer(offer).build();
        offer.setProjectProgress(projectProgressRepository.save(projectProgress));
        offer.setStatus(OfferStatus.accepted);
        offerRepository.updateStatusOfSameProject(OfferStatus.dropped, id);
        offerRepository.save(offer);
        projectService.updateInternalFromOffer(id, ProjectStatus.inProgress, offer.getWorker().getId());
        fcmService.sendNotification("Offer Accepted", "One of your offers has been accepted.", offer.getWorker().getUser().getDevice_token());
        return Map.of("message", "Offer Accepted");
    }
    public Map<String, String> reject(Long id) throws FirebaseMessagingException {
        Integer userId = auditAware.getCurrentAuditor().orElseThrow(() -> new RuntimeException("Auditor ID not found"));
        if(!offerRepository.existsByProject_Client_UserId(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Project does not belong to the user");
        }
        Offer offer = offerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Offer Not Found"));
        if(!offer.getStatus().equals(OfferStatus.pending)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Offer should be pending when rejected");
        }
        updateInternal(id, OfferStatus.rejected);
        fcmService.sendNotification("Offer Rejected", "One of your offers has been rejected.", offer.getWorker().getUser().getDevice_token());
        return Map.of("message", "Offer Rejected");
    }


    //-------ADMIN--------
    public Map<String, String> adminDelete(Long offerId){
        offerRepository.deleteById(offerId);
//        fcmService.sendNotification("Offer Deleted", "One of your offers has been deleted.", offer.getWorker().getUser().getDevice_token());
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
