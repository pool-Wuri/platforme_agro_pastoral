package com.anptic.agropastoral.dto.reservation;

import com.anptic.agropastoral.dto.offer.OfferResponse;
import com.anptic.agropastoral.dto.user.UserResponse;
import com.anptic.agropastoral.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponse {

    private UUID id;
    private OfferResponse offer;
    private UserResponse buyer;
    private Double quantity;
    private LocalDateTime reservedUntil;
    private ReservationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
