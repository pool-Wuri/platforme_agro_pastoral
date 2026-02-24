package com.anptic.agropastoral.service.impl;

import com.anptic.agropastoral.dto.stockmovement.StockMovementRequest;
import com.anptic.agropastoral.dto.stockmovement.StockMovementResponse;
import com.anptic.agropastoral.mappers.StockMovementMapper;
import com.anptic.agropastoral.model.Stock;
import com.anptic.agropastoral.model.StockMovement;
import com.anptic.agropastoral.model.User;
import com.anptic.agropastoral.repository.StockMovementRepository;
import com.anptic.agropastoral.repository.StockRepository;
import com.anptic.agropastoral.repository.UserRepository;
import com.anptic.agropastoral.service.StockMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockMovementServiceImpl implements StockMovementService {

    private final StockMovementRepository stockMovementRepository;
    private final StockMovementMapper stockMovementMapper;
    private final StockRepository stockRepository;
    private final UserRepository userRepository;

    @Override
    public StockMovementResponse createStockMovement(StockMovementRequest stockMovementRequest) {
        User currentUser = getCurrentUser();
        Stock stock = stockRepository.findById(stockMovementRequest.getStockId())
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        StockMovement stockMovement = stockMovementMapper.toStockMovement(stockMovementRequest);
        stockMovement.setStock(stock);
        stockMovement.setUser(currentUser);
        stockMovement.setTimestamp(LocalDateTime.now());

        double previousQuantity = stock.getQuantity();
        double newQuantity;

        switch (stockMovementRequest.getType()) {
            case ENTRY:
                newQuantity = previousQuantity + stockMovementRequest.getQuantity();
                break;
            case EXIT:
                newQuantity = previousQuantity - stockMovementRequest.getQuantity();
                break;
            case TRANSFER:
                // For transfer, we assume the quantity is deducted from this stock
                // and will be added to another stock in a separate transaction.
                newQuantity = previousQuantity - stockMovementRequest.getQuantity();
                break;
            default:
                throw new IllegalArgumentException("Invalid movement type");
        }

        if (newQuantity < 0) {
            throw new RuntimeException("Insufficient stock");
        }

        stock.setQuantity(newQuantity);
        stock.setLastUpdated(LocalDateTime.now());
        stockRepository.save(stock);

        stockMovement.setPreviousQuantity(previousQuantity);
        stockMovement.setNewQuantity(newQuantity);

        return stockMovementMapper.toStockMovementResponse(stockMovementRepository.save(stockMovement));
    }

    @Override
    public List<StockMovementResponse> getStockMovements(UUID stockId) {
        return stockMovementRepository.findAll().stream()
                .filter(movement -> movement.getStock().getId().equals(stockId))
                .map(stockMovementMapper::toStockMovementResponse)
                .collect(Collectors.toList());
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
