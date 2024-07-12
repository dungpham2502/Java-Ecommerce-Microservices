package com.example.ecommerce.order.dto;

import com.example.ecommerce.order.entity.PaymentMethod;

import java.math.BigDecimal;

public record OrderResponse(
        Integer id,
        String reference,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer customerId
) {

}
