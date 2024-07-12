package com.example.ecommerce.product.dto;

import java.math.BigDecimal;

public record ProductDetailsResponse(
        Integer id,
        String name,
        String description,
        BigDecimal price,
        double quantity
) {
}
