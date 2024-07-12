package com.example.ecommerce.cart.dto;

import com.example.ecommerce.item.dto.CartItemRequest;

import java.util.List;

public record CartRequest(
        Integer customerId,
        List<CartItemRequest> items
){

}
