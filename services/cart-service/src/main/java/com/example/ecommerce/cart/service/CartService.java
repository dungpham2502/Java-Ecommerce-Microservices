package com.example.ecommerce.cart.service;

import com.example.ecommerce.cart.dto.CartRequest;
import com.example.ecommerce.cart.dto.CartResponse;
import com.example.ecommerce.item.dto.CartItemRequest;

public interface CartService {
    Integer createCart(CartRequest cartRequest);
    void addItemToCart(Integer cartId, CartItemRequest cartItemRequest);
    CartResponse getCart(Integer customerId);
    void removeFromCart(Integer customerId, Integer productId);
    void clearCart(Integer customerId);
}
