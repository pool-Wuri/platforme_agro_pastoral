package com.anptic.agropastoral.service;

import com.anptic.agropastoral.dto.product.ProductRequest;
import com.anptic.agropastoral.dto.product.ProductResponse;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductResponse createProduct(ProductRequest productRequest);
    List<ProductResponse> getAllProducts();
    ProductResponse getProductById(UUID id);
    ProductResponse updateProduct(UUID id, ProductRequest productRequest);
    void deleteProduct(UUID id);
}
