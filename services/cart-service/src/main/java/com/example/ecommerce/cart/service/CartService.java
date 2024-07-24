package com.example.ecommerce.cart.service;

import com.example.ecommerce.cart.dto.CartRequest;
import com.example.ecommerce.cart.dto.CartResponse;
import com.example.ecommerce.customer.dto.CustomerResponse;
import com.example.ecommerce.item.dto.CartItemRequest;

import java.util.Optional;

public interface CartService {
    Integer createCart(CartRequest cartRequest);
    void addItemToCart(Integer cartId, CartItemRequest cartItemRequest);
    CartResponse getCart(Integer customerId);
    void removeFromCart(Integer customerId, Integer productId);
    void clearCart(Integer customerId);
    Optional<CustomerResponse> getCustomer(Integer customerId);
}
