package com.anptic.agropastoral.service;

import com.anptic.agropastoral.dto.stock.StockRequest;
import com.anptic.agropastoral.dto.stock.StockResponse;

import java.util.List;
import java.util.UUID;

public interface StockService {
    StockResponse createStock(StockRequest stockRequest);
    List<StockResponse> getUserStocks();
    StockResponse getStockById(UUID id);
    StockResponse updateStock(UUID id, StockRequest stockRequest);
    void deleteStock(UUID id);
}
