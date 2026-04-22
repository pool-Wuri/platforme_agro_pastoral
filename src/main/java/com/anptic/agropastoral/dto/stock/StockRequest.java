package com.anptic.agropastoral.dto.stock;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockRequest {

    @NotNull(message = "L'ID du produit est obligatoire")
    private UUID productId;

    @NotNull(message = "La quantité est obligatoire")
    private Double quantity;

    @NotBlank(message = "La région est obligatoire")
    private String region;

    @NotNull(message = "Le seuil d'alerte est obligatoire")
    private Double warningThreshold;

    @NotNull(message = "Le seuil critique est obligatoire")
    private Double criticalThreshold;
}
