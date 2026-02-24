package com.anptic.agropastoral.controller;

import com.anptic.agropastoral.dto.reservation.ReservationRequest;
import com.anptic.agropastoral.dto.reservation.ReservationResponse;
import com.anptic.agropastoral.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ACHETEUR')")
    public ResponseEntity<ReservationResponse> createReservation(@Valid @RequestBody ReservationRequest reservationRequest) {
        return new ResponseEntity<>(reservationService.createReservation(reservationRequest), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ACHETEUR') or hasRole('ROLE_PRODUCTEUR')")
    public ResponseEntity<List<ReservationResponse>> getUserReservations() {
        return ResponseEntity.ok(reservationService.getUserReservations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> getReservationById(@PathVariable UUID id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    @PutMapping("/{id}/confirm")
    @PreAuthorize("hasRole('ROLE_PRODUCTEUR')")
    public ResponseEntity<ReservationResponse> confirmReservation(@PathVariable UUID id) {
        return ResponseEntity.ok(reservationService.confirmReservation(id));
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ROLE_PRODUCTEUR')")
    public ResponseEntity<ReservationResponse> rejectReservation(@PathVariable UUID id) {
        return ResponseEntity.ok(reservationService.rejectReservation(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ACHETEUR')")
    public ResponseEntity<Void> cancelReservation(@PathVariable UUID id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }
}
