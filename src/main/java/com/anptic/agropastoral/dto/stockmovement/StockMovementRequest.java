package com.anptic.agropastoral.dto.stockmovement;

import com.anptic.agropastoral.enums.MovementType;
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
public class StockMovementRequest {

    @NotNull(message = "L'ID du stock est obligatoire")
    private UUID stockId;

    @NotNull(message = "Le type de mouvement est obligatoire")
    private MovementType type;

    @NotNull(message = "La quantité est obligatoire")
    private Double quantity;

    private String reason;
}
