package com.anptic.agropastoral.service.impl;

import com.anptic.agropastoral.dto.reservation.ReservationRequest;
import com.anptic.agropastoral.dto.reservation.ReservationResponse;
import com.anptic.agropastoral.enums.ReservationStatus;
import com.anptic.agropastoral.mappers.ReservationMapper;
import com.anptic.agropastoral.model.Offer;
import com.anptic.agropastoral.model.Reservation;
import com.anptic.agropastoral.model.User;
import com.anptic.agropastoral.repository.OfferRepository;
import com.anptic.agropastoral.repository.ReservationRepository;
import com.anptic.agropastoral.repository.UserRepository;
import com.anptic.agropastoral.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;

    @Override
    public ReservationResponse createReservation(ReservationRequest reservationRequest) {
        User currentUser = getCurrentUser();
        Offer offer = offerRepository.findById(reservationRequest.getOfferId())
                .orElseThrow(() -> new RuntimeException("Offer not found"));

        if (offer.getQuantity() < reservationRequest.getQuantity()) {
            throw new RuntimeException("Insufficient quantity in offer");
        }

        Reservation reservation = reservationMapper.toReservation(reservationRequest);
        reservation.setBuyer(currentUser);
        reservation.setOffer(offer);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setCreatedAt(LocalDateTime.now());

        return reservationMapper.toReservationResponse(reservationRepository.save(reservation));
    }

    @Override
    public List<ReservationResponse> getUserReservations() {
        User currentUser = getCurrentUser();
        return reservationRepository.findAll().stream()
                .filter(reservation -> reservation.getBuyer().getId().equals(currentUser.getId()))
                .map(reservationMapper::toReservationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ReservationResponse getReservationById(UUID id) {
        return reservationRepository.findById(id)
                .map(reservationMapper::toReservationResponse)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
    }

    @Override
    public ReservationResponse confirmReservation(UUID id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setUpdatedAt(LocalDateTime.now());
        return reservationMapper.toReservationResponse(reservationRepository.save(reservation));
    }

    @Override
    public ReservationResponse rejectReservation(UUID id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        reservation.setStatus(ReservationStatus.REJECTED);
        reservation.setUpdatedAt(LocalDateTime.now());
        return reservationMapper.toReservationResponse(reservationRepository.save(reservation));
    }

    @Override
    public void cancelReservation(UUID id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setUpdatedAt(LocalDateTime.now());
        reservationRepository.save(reservation);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
