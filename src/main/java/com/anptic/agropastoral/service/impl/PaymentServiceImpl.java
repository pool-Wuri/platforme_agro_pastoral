package com.anptic.agropastoral.service.impl;

import com.anptic.agropastoral.dto.payment.PaymentRequest;
import com.anptic.agropastoral.dto.payment.PaymentResponse;
import com.anptic.agropastoral.enums.PaymentStatus;
import com.anptic.agropastoral.enums.ReservationStatus;
import com.anptic.agropastoral.model.Payment;
import com.anptic.agropastoral.model.Reservation;
import com.anptic.agropastoral.model.Offer;
import com.anptic.agropastoral.repository.PaymentRepository;
import com.anptic.agropastoral.repository.ReservationRepository;
import com.anptic.agropastoral.service.PaymentService;
import com.anptic.agropastoral.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final StockService stockService;

    @Override
    @Transactional
    public PaymentResponse processPayment(PaymentRequest paymentRequest) {
        Reservation reservation = reservationRepository.findById(paymentRequest.getReservationId())
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!reservation.getBuyer().getEmail().equals(currentUserEmail)) {
            throw new RuntimeException("You can only pay your own reservation");
        }

        if (reservation.getReservedUntil() != null && reservation.getReservedUntil().isBefore(LocalDateTime.now())) {
            reservation.setStatus(ReservationStatus.EXPIRED);
            reservation.setUpdatedAt(LocalDateTime.now());
            reservationRepository.save(reservation);
            throw new RuntimeException("Payment deadline reached. Reservation expired");
        }

        if (reservation.getStatus() == ReservationStatus.REJECTED
                || reservation.getStatus() == ReservationStatus.CANCELLED
                || reservation.getStatus() == ReservationStatus.EXPIRED
                || reservation.getStatus() == ReservationStatus.PAID) {
            throw new RuntimeException("Reservation cannot be paid in its current status");
        }

        Offer offer = reservation.getOffer();
        if (offer.getQuantity() < reservation.getQuantity()) {
            throw new RuntimeException("Offer quantity is no longer sufficient for this payment");
        }

        paymentRepository.findByReservationId(reservation.getId()).ifPresent(existingPayment -> {
            if (existingPayment.getStatus() == PaymentStatus.SUCCESS) {
                throw new RuntimeException("Reservation already paid");
            }
        });

        double amount = reservation.getQuantity() * reservation.getOffer().getPricePerUnit();
        Payment payment = paymentRepository.findByReservationId(reservation.getId())
                .orElse(Payment.builder().reservation(reservation).build());

        payment.setAmount(amount);
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        payment.setTransactionId(paymentRequest.getTransactionId());
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setTimestamp(LocalDateTime.now());

        offer.setQuantity(offer.getQuantity() - reservation.getQuantity());

        reservation.setStatus(ReservationStatus.PAID);
        reservation.setPaidAt(payment.getTimestamp());
        reservation.setUpdatedAt(payment.getTimestamp());

        stockService.decrementStockAfterPayment(
                offer.getProductor().getId(),
                offer.getProduct().getId(),
                offer.getRegion() != null ? offer.getRegion().getName() : null,
                reservation.getQuantity(),
                "Reservation paid: " + reservation.getId()
        );

        Payment savedPayment = paymentRepository.save(payment);
        reservationRepository.save(reservation);

        return PaymentResponse.builder()
                .id(savedPayment.getId())
                .reservationId(reservation.getId())
                .amount(savedPayment.getAmount())
                .paymentMethod(savedPayment.getPaymentMethod())
                .status(savedPayment.getStatus())
                .transactionId(savedPayment.getTransactionId())
                .timestamp(savedPayment.getTimestamp())
                .build();
    }
}
