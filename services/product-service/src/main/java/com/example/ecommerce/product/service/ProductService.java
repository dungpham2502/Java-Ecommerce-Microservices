package com.example.ecommerce.product.service;

import com.example.ecommerce.product.dto.*;

import java.util.List;

public interface ProductService {
    Integer createProduct(ProductRequest request);
    List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> requests);
    ProductResponse findById(Integer id);
    List<ProductResponse> findAll();
    List<ProductAvailabilityResponse> checkProductAvailability(List<Integer> productIds, List<Double> quantities);

}
