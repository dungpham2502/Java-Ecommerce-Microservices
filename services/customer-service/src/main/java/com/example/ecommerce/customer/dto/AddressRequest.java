package com.example.ecommerce.customer.dto;

public record AddressRequest(
        String street,
        String city,
        String state,
        String zipCode
) {
}
