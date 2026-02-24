package com.anptic.agropastoral.controller;

import com.anptic.agropastoral.dto.filiere.FiliereRequest;
import com.anptic.agropastoral.dto.filiere.FiliereResponse;
import com.anptic.agropastoral.service.FiliereService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/filieres")
@RequiredArgsConstructor
public class FiliereController {

    private final FiliereService filiereService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<FiliereResponse> createFiliere(@Valid @RequestBody FiliereRequest filiereRequest) {
        return new ResponseEntity<>(filiereService.createFiliere(filiereRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FiliereResponse>> getAllFilieres() {
        return ResponseEntity.ok(filiereService.getAllFilieres());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FiliereResponse> getFiliereById(@PathVariable UUID id) {
        return ResponseEntity.ok(filiereService.getFiliereById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<FiliereResponse> updateFiliere(@PathVariable UUID id, @Valid @RequestBody FiliereRequest filiereRequest) {
        return ResponseEntity.ok(filiereService.updateFiliere(id, filiereRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteFiliere(@PathVariable UUID id) {
        filiereService.deleteFiliere(id);
        return ResponseEntity.noContent().build();
    }
}
