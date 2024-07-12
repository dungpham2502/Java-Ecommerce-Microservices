package com.example.ecommerce.payment.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record Customer(
        Integer id,
        String firstName,
        String lastName,
        String email
) {
}
