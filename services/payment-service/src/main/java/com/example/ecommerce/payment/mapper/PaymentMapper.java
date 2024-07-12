package com.example.ecommerce.payment.mapper;

import com.example.ecommerce.payment.dto.PaymentRequest;
import com.example.ecommerce.payment.entity.Payment;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentMapper {
    public Payment toPayment(PaymentRequest paymentRequest) {
        if (paymentRequest == null) {
            return null;
        }
        return Payment.builder()
                .amount(paymentRequest.amount())
                .paymentMethod(paymentRequest.paymentMethod())
                .orderId(paymentRequest.orderId())
                .lastModifiedDate(LocalDateTime.now())
                .createdDate(LocalDateTime.now())
                .build();
    }
}
