package com.project1.offer;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.project1.offer.data.CreateOfferRequest;
import com.project1.offer.data.OfferResponse;
import com.project1.offer.data.UpdateOfferRequest;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/offers")
@RequiredArgsConstructor
public class
 OfferController {

    private final OfferService offerService;

    @GetMapping("/byProject/{id}")
    public ResponseEntity<Map<String,List<OfferResponse>>> getProjectOffers(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of("offers", offerService.getByProject(id)));
    }

    @GetMapping("/byWorker/{id}")
    public ResponseEntity<Map<String, List<OfferResponse>>> getByWorker(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of("offers",offerService.getByWorker(id)));
    }


    @PostMapping
    @PreAuthorize("hasAnyRole('WORKER','CLIENT_WORKER')")
    public ResponseEntity<OfferResponse> create(@RequestBody CreateOfferRequest createOfferRequest) throws FirebaseMessagingException {
        return ResponseEntity.ok(offerService.create(createOfferRequest));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('WORKER','CLIENT_WORKER')")
    public ResponseEntity<OfferResponse> update(@PathVariable @Parameter Long id, @RequestBody UpdateOfferRequest updateOfferRequest) throws ResponseStatusException {
        return  ResponseEntity.ok(offerService.update(id, updateOfferRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('WORKER','CLIENT_WORKER')")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) throws ResponseStatusException{
        return  ResponseEntity.ok(offerService.delete(id));
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> adminDelete(@PathVariable Long id) throws ResponseStatusException{
        return  ResponseEntity.ok(offerService.adminDelete(id));
    }


    @PostMapping("/accept/{id}")
    @PreAuthorize("hasAnyRole('CLIENT','CLIENT_WORKER')")
    public ResponseEntity<Map<String, String>> accept(@PathVariable Long id) throws ResponseStatusException, FirebaseMessagingException {
        return  ResponseEntity.ok(offerService.accept(id));
    }

    @PostMapping("/reject/{id}")
    @PreAuthorize("hasAnyRole('CLIENT','CLIENT_WORKER')")
    public ResponseEntity<Map<String, String>> reject(@PathVariable Long id) throws ResponseStatusException, FirebaseMessagingException {
        return  ResponseEntity.ok(offerService.reject(id));
    }
}
