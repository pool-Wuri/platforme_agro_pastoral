package com.anptic.agropastoral.dto.offer;

import com.anptic.agropastoral.dto.product.ProductResponse;
import com.anptic.agropastoral.dto.user.UserResponse;
import com.anptic.agropastoral.dto.region.RegionResponse;
import com.anptic.agropastoral.enums.OfferStatus;
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
public class OfferResponse {

    private UUID id;
    private UserResponse productor;
    private ProductResponse product;
    private Double quantity;
    private Double pricePerUnit;
    private RegionResponse region;
    private OfferStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime expiryDate;
}
