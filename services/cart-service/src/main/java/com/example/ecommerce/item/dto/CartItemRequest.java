package com.example.ecommerce.item.dto;

import com.example.ecommerce.cart.entity.Cart;

public record CartItemRequest(
        Integer productId,
        double quantity,
        Cart cartId
) {
}
