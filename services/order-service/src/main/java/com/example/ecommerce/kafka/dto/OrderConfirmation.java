package com.example.ecommerce.kafka.dto;

import com.example.ecommerce.customer.dto.CustomerResponse;
import com.example.ecommerce.order.entity.PaymentMethod;
import com.example.ecommerce.product.dto.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        String customerFirstname,
        String customerLastname,
        String customerEmail,
        List<PurchaseResponse> products
) {
}
