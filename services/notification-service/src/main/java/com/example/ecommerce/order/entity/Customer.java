package com.example.ecommerce.order.entity;


public record Customer(
        Integer id,
        String firstName,
        String lastName,
        String email
) {
}
