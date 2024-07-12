package com.example.ecommerce.payment.dto;

import com.example.ecommerce.customer.dto.CustomerResponse;
import com.example.ecommerce.order.entity.PaymentMethod;

import java.math.BigDecimal;


public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}
