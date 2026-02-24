package com.anptic.agropastoral.dto.reservation;

import jakarta.validation.constraints.NotNull;
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
public class ReservationRequest {

    @NotNull(message = "L'ID de l'offre est obligatoire")
    private UUID offerId;

    @NotNull(message = "La quantité est obligatoire")
    private Double quantity;

    private LocalDateTime reservedUntil;
}
