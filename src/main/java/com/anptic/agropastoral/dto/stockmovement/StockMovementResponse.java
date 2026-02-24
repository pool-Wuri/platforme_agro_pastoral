package com.anptic.agropastoral.dto.stockmovement;

import com.anptic.agropastoral.dto.stock.StockResponse;
import com.anptic.agropastoral.dto.user.UserResponse;
import com.anptic.agropastoral.enums.MovementType;
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
public class StockMovementResponse {

    private UUID id;
    private StockResponse stock;
    private MovementType type;
    private Double quantity;
    private String reason;
    private Double previousQuantity;
    private Double newQuantity;
    private LocalDateTime timestamp;
    private UserResponse user;
}
