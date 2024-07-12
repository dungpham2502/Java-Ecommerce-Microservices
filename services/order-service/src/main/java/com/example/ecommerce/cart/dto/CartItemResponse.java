package com.example.ecommerce.cart.dto;

import java.math.BigDecimal;

public record CartItemResponse(
        Integer productId,
        String name,
        String description,
        BigDecimal price,
        double quantity
) {
}
