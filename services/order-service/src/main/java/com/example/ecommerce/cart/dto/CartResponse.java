package com.example.ecommerce.cart.dto;

import java.util.List;

public record CartResponse(
        Integer id,
        Integer customerId,
        List<CartItemResponse> products
) {}
