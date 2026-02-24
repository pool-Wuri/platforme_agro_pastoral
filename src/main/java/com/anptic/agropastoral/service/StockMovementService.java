package com.anptic.agropastoral.service;

import com.anptic.agropastoral.dto.stockmovement.StockMovementRequest;
import com.anptic.agropastoral.dto.stockmovement.StockMovementResponse;

import java.util.List;
import java.util.UUID;

public interface StockMovementService {
    StockMovementResponse createStockMovement(StockMovementRequest stockMovementRequest);
    List<StockMovementResponse> getStockMovements(UUID stockId);
}
