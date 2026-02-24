package com.anptic.agropastoral.service;

import com.anptic.agropastoral.dto.reservation.ReservationRequest;
import com.anptic.agropastoral.dto.reservation.ReservationResponse;

import java.util.List;
import java.util.UUID;

public interface ReservationService {
    ReservationResponse createReservation(ReservationRequest reservationRequest);
    List<ReservationResponse> getUserReservations();
    ReservationResponse getReservationById(UUID id);
    ReservationResponse confirmReservation(UUID id);
    ReservationResponse rejectReservation(UUID id);
    void cancelReservation(UUID id);
}
