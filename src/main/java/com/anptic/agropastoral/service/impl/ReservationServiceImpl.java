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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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

    @Value("${app.reservation.payment-deadline-hours:24}")
    private long paymentDeadlineHours;

    @Override
    public ReservationResponse createReservation(ReservationRequest reservationRequest) {
        User currentUser = getCurrentUser();
        Offer offer = offerRepository.findById(reservationRequest.getOfferId())
                .orElseThrow(() -> new RuntimeException("Offer not found"));

        double reservedQuantity = reservationRepository.findByOfferIdAndStatusIn(
                        offer.getId(),
                        List.of(ReservationStatus.PENDING, ReservationStatus.CONFIRMED))
                .stream()
                .mapToDouble(Reservation::getQuantity)
                .sum();

        double availableQuantity = offer.getQuantity() - reservedQuantity;
        if (availableQuantity < reservationRequest.getQuantity()) {
            throw new RuntimeException("Insufficient available quantity in offer");
        }

        Reservation reservation = reservationMapper.toReservation(reservationRequest);
        reservation.setBuyer(currentUser);
        reservation.setOffer(offer);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setUpdatedAt(LocalDateTime.now());
        reservation.setReservedUntil(resolveReservationDeadline(reservationRequest.getReservedUntil()));

        return reservationMapper.toReservationResponse(reservationRepository.save(reservation));
    }

    @Override
    public List<ReservationResponse> getUserReservations() {
        User currentUser = getCurrentUser();
        if (currentUser.getRole().name().equals("ROLE_PRODUCTEUR")) {
            return reservationRepository.findByOfferProductorId(currentUser.getId()).stream()
                    .map(reservationMapper::toReservationResponse)
                    .collect(Collectors.toList());
        }

        return reservationRepository.findByBuyerId(currentUser.getId()).stream()
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
        ensureReservationIsStillActive(reservation);
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
        if (reservation.getStatus() == ReservationStatus.PAID) {
            throw new RuntimeException("A paid reservation cannot be cancelled");
        }
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setUpdatedAt(LocalDateTime.now());
        reservationRepository.save(reservation);
    }

    @Override
    @Scheduled(cron = "${app.reservation.expiration-check-cron:0 */5 * * * *}")
    public void expireReservationsPastDeadline() {
        LocalDateTime now = LocalDateTime.now();
        List<Reservation> expiredReservations = reservationRepository.findByStatusInAndReservedUntilBefore(
                List.of(ReservationStatus.PENDING, ReservationStatus.CONFIRMED),
                now
        );

        expiredReservations.forEach(reservation -> {
            reservation.setStatus(ReservationStatus.EXPIRED);
            reservation.setUpdatedAt(now);
        });

        if (!expiredReservations.isEmpty()) {
            reservationRepository.saveAll(expiredReservations);
        }
    }

    private void ensureReservationIsStillActive(Reservation reservation) {
        if (reservation.getReservedUntil() != null && reservation.getReservedUntil().isBefore(LocalDateTime.now())) {
            reservation.setStatus(ReservationStatus.EXPIRED);
            reservation.setUpdatedAt(LocalDateTime.now());
            reservationRepository.save(reservation);
            throw new RuntimeException("Payment deadline reached. Reservation expired");
        }
    }

    private LocalDateTime resolveReservationDeadline(LocalDateTime requestedDeadline) {
        LocalDateTime defaultDeadline = LocalDateTime.now().plusHours(paymentDeadlineHours);
        if (requestedDeadline == null) {
            return defaultDeadline;
        }
        if (requestedDeadline.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Reservation deadline must be in the future");
        }
        return requestedDeadline;
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
