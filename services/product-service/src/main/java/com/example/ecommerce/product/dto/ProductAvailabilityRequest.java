package com.example.ecommerce.product.dto;

public record ProductAvailabilityRequest(
    Integer productId,
    double quantity
)
{}
