package com.anptic.agropastoral.service.impl;

import com.anptic.agropastoral.dto.stock.StockRequest;
import com.anptic.agropastoral.dto.stock.StockResponse;
import com.anptic.agropastoral.mappers.StockMapper;
import com.anptic.agropastoral.model.Product;
import com.anptic.agropastoral.model.Stock;
import com.anptic.agropastoral.model.User;
import com.anptic.agropastoral.repository.ProductRepository;
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
        stock.setLastUpdated(LocalDateTime.now());

        return stockMapper.toStockResponse(stockRepository.save(stock));
    }

    @Override
    public void deleteStock(UUID id) {
        stockRepository.deleteById(id);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
