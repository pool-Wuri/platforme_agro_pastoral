package com.anptic.agropastoral.repository;

import com.anptic.agropastoral.enums.ReservationStatus;
import com.anptic.agropastoral.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    List<Reservation> findByBuyerId(UUID buyerId);
    List<Reservation> findByOfferProductorId(UUID productorId);
    List<Reservation> findByOfferIdAndStatusIn(UUID offerId, Collection<ReservationStatus> statuses);
    List<Reservation> findByStatusInAndReservedUntilBefore(Collection<ReservationStatus> statuses, LocalDateTime reservedUntil);
}
