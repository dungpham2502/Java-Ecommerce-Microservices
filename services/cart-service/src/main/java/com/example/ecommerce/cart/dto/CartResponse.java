package com.example.ecommerce.cart.dto;

import com.example.ecommerce.item.dto.CartItemResponse;

import java.util.List;

public record CartResponse(
        Integer id,
        Integer customerId,
        List<CartItemResponse> products
) {}
