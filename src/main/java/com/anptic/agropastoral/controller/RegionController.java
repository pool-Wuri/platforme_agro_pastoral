package com.anptic.agropastoral.controller;

import com.anptic.agropastoral.dto.region.RegionRequest;
import com.anptic.agropastoral.dto.region.RegionResponse;
import com.anptic.agropastoral.service.RegionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RegionResponse> createRegion(@Valid @RequestBody RegionRequest regionRequest) {
        return new ResponseEntity<>(regionService.createRegion(regionRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RegionResponse>> getAllRegions() {
        return ResponseEntity.ok(regionService.getAllRegions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionResponse> getRegionById(@PathVariable UUID id) {
        return ResponseEntity.ok(regionService.getRegionById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RegionResponse> updateRegion(@PathVariable UUID id, @Valid @RequestBody RegionRequest regionRequest) {
        return ResponseEntity.ok(regionService.updateRegion(id, regionRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteRegion(@PathVariable UUID id) {
        regionService.deleteRegion(id);
        return ResponseEntity.noContent().build();
    }
}
