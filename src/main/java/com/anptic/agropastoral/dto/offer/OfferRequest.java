package com.anptic.agropastoral.dto.offer;

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
public class OfferRequest {

    @NotNull(message = "L'ID du produit est obligatoire")
    private UUID productId;

    @NotNull(message = "La quantité est obligatoire")
    private Double quantity;

    @NotNull(message = "Le prix par unité est obligatoire")
    private Double pricePerUnit;

    @NotNull(message = "L'ID de la région est obligatoire")
    private UUID regionId;

    private LocalDateTime expiryDate;
}
