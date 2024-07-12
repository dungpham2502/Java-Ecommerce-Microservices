package com.example.ecommerce.payment.service;

import com.example.ecommerce.payment.dto.PaymentRequest;

public interface PaymentService {
    Integer createPayment(PaymentRequest paymentRequest);
}
