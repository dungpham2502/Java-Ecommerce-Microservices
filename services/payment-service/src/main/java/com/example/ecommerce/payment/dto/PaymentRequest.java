package com.example.ecommerce.payment.dto;

import com.example.ecommerce.payment.entity.Customer;
import com.example.ecommerce.payment.entity.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
    BigDecimal amount,
    PaymentMethod paymentMethod,
    Integer orderId,
    String orderReference,
    Customer customer
) {
}
