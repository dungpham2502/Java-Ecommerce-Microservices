package com.example.ecommerce.product.dto;

import java.math.BigDecimal;

public record ProductResponse(
        Integer productId,
        String name,
        String description,
        BigDecimal price,
        double availableQuantity
) {
}
