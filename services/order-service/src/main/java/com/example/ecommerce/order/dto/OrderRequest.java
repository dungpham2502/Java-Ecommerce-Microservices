package com.example.ecommerce.order.dto;

import com.example.ecommerce.order.entity.PaymentMethod;
import com.example.ecommerce.product.dto.PurchaseRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequest (
        Integer customerId,
        PaymentMethod paymentMethod
){

}
