package com.example.ecommerce.customer.dto;

import com.example.ecommerce.customer.entity.Address;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest(

        @NotNull(message = "Customer firstname is required")
        String firstName,

        @NotNull(message = "Customer lastname is required")
        String lastName,

        @NotNull(message = "Customer email is required")
        String email,

        AddressRequest address
)
{
}

