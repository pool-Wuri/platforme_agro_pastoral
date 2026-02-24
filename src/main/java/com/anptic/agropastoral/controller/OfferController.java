package com.anptic.agropastoral.controller;

import com.anptic.agropastoral.dto.offer.OfferRequest;
import com.anptic.agropastoral.dto.offer.OfferResponse;
import com.anptic.agropastoral.service.OfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/offers")
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_PRODUCTEUR')")
    public ResponseEntity<OfferResponse> createOffer(@Valid @RequestBody OfferRequest offerRequest) {
        return new ResponseEntity<>(offerService.createOffer(offerRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OfferResponse>> getAllOffers() {
        return ResponseEntity.ok(offerService.getAllOffers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferResponse> getOfferById(@PathVariable UUID id) {
        return ResponseEntity.ok(offerService.getOfferById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PRODUCTEUR') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<OfferResponse> updateOffer(@PathVariable UUID id, @Valid @RequestBody OfferRequest offerRequest) {
        return ResponseEntity.ok(offerService.updateOffer(id, offerRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PRODUCTEUR') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteOffer(@PathVariable UUID id) {
        offerService.deleteOffer(id);
        return ResponseEntity.noContent().build();
    }
}
