package com.anptic.agropastoral.service.impl;

import com.anptic.agropastoral.dto.stock.StockRequest;
import com.anptic.agropastoral.dto.stock.StockResponse;
import com.anptic.agropastoral.enums.AlertLevel;
import com.anptic.agropastoral.enums.MovementType;
import com.anptic.agropastoral.mappers.StockMapper;
import com.anptic.agropastoral.model.Product;
import com.anptic.agropastoral.model.Stock;
import com.anptic.agropastoral.model.StockMovement;
import com.anptic.agropastoral.model.User;
import com.anptic.agropastoral.repository.ProductRepository;
import com.anptic.agropastoral.repository.StockMovementRepository;
import com.anptic.agropastoral.repository.StockRepository;
import com.anptic.agropastoral.repository.UserRepository;
import com.anptic.agropastoral.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final StockMovementRepository stockMovementRepository;

    @Override
    public StockResponse createStock(StockRequest stockRequest) {
        User currentUser = getCurrentUser();
        Product product = productRepository.findById(stockRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Stock stock = stockMapper.toStock(stockRequest);
        stock.setUser(currentUser);
        stock.setProduct(product);
        stock.setCreatedAt(LocalDateTime.now());
        stock.setLastUpdated(LocalDateTime.now());
        validateThresholds(stockRequest.getWarningThreshold(), stockRequest.getCriticalThreshold());
        stock.setWarningThreshold(stockRequest.getWarningThreshold());
        stock.setCriticalThreshold(stockRequest.getCriticalThreshold());
        stock.setAlertLevel(calculateAlertLevel(stock.getQuantity(), stock.getWarningThreshold(), stock.getCriticalThreshold()));

        return stockMapper.toStockResponse(stockRepository.save(stock));
    }

    @Override
    public List<StockResponse> getUserStocks() {
        User currentUser = getCurrentUser();
        return stockRepository.findAll().stream()
                .filter(stock -> stock.getUser().getId().equals(currentUser.getId()))
                .map(stockMapper::toStockResponse)
                .collect(Collectors.toList());
    }

    @Override
    public StockResponse getStockById(UUID id) {
        return stockRepository.findById(id)
                .map(stockMapper::toStockResponse)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
    }

    @Override
    public StockResponse updateStock(UUID id, StockRequest stockRequest) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        Product product = productRepository.findById(stockRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        stock.setProduct(product);
        stock.setQuantity(stockRequest.getQuantity());
        stock.setRegion(stockRequest.getRegion());
        validateThresholds(stockRequest.getWarningThreshold(), stockRequest.getCriticalThreshold());
        stock.setWarningThreshold(stockRequest.getWarningThreshold());
        stock.setCriticalThreshold(stockRequest.getCriticalThreshold());
        stock.setAlertLevel(calculateAlertLevel(stock.getQuantity(), stock.getWarningThreshold(), stock.getCriticalThreshold()));
        stock.setLastUpdated(LocalDateTime.now());

        return stockMapper.toStockResponse(stockRepository.save(stock));
    }

    @Override
    public void deleteStock(UUID id) {
        stockRepository.deleteById(id);
    }

    @Override
    public void decrementStockAfterPayment(UUID productorId, UUID productId, String region, Double quantity, String reason) {
        Stock stock = stockRepository.findFirstByUserIdAndProductIdAndRegionIgnoreCase(productorId, productId, region)
                .or(() -> stockRepository.findFirstByUserIdAndProductId(productorId, productId))
                .orElseThrow(() -> new RuntimeException("Stock not found for productor and product"));

        if (stock.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock to complete payment");
        }

        double previousQuantity = stock.getQuantity();
        double newQuantity = previousQuantity - quantity;
        stock.setQuantity(newQuantity);
        stock.setAlertLevel(calculateAlertLevel(newQuantity, stock.getWarningThreshold(), stock.getCriticalThreshold()));
        stock.setLastUpdated(LocalDateTime.now());
        Stock savedStock = stockRepository.save(stock);

        stockMovementRepository.save(StockMovement.builder()
                .stock(savedStock)
                .type(MovementType.EXIT)
                .quantity(quantity)
                .reason(reason)
                .previousQuantity(previousQuantity)
                .newQuantity(newQuantity)
                .timestamp(LocalDateTime.now())
                .user(savedStock.getUser())
                .build());
    }

    private void validateThresholds(Double warningThreshold, Double criticalThreshold) {
        if (warningThreshold < 0 || criticalThreshold < 0) {
            throw new RuntimeException("Alert thresholds must be positive");
        }
        if (warningThreshold < criticalThreshold) {
            throw new RuntimeException("Warning threshold must be greater than or equal to critical threshold");
        }
    }

    private AlertLevel calculateAlertLevel(Double quantity, Double warningThreshold, Double criticalThreshold) {
        if (warningThreshold == null || criticalThreshold == null) {
            return AlertLevel.NORMAL;
        }
        if (quantity <= criticalThreshold) {
            return AlertLevel.CRITICAL;
        }
        if (quantity <= warningThreshold) {
            return AlertLevel.WARNING;
        }
        return AlertLevel.NORMAL;
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
