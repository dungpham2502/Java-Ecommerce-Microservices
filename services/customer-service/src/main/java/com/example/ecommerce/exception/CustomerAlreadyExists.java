package com.example.ecommerce.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerAlreadyExists extends RuntimeException{
    private final String message;
}
