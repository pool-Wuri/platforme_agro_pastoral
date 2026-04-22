package com.anptic.agropastoral.repository;

import com.anptic.agropastoral.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockRepository extends JpaRepository<Stock, UUID> {
    Optional<Stock> findFirstByUserIdAndProductIdAndRegionIgnoreCase(UUID userId, UUID productId, String region);
    Optional<Stock> findFirstByUserIdAndProductId(UUID userId, UUID productId);
}
