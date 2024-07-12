package com.example.ecommerce.customer.dto;

public record CustomerResponse (
    Integer id,
    String firstName,
    String lastName,
    String email
) {

}
