package com.anptic.agropastoral.dto.stock;

import com.anptic.agropastoral.dto.product.ProductResponse;
import com.anptic.agropastoral.dto.user.UserResponse;
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
public class StockResponse {

    private UUID id;
    private UserResponse user;
    private ProductResponse product;
    private Double quantity;
    private String region;
    private LocalDateTime lastUpdated;
}
