package com.example.ecommerce.payment.service;

import com.example.ecommerce.notification.kafka.NotificationProducer;
import com.example.ecommerce.notification.dto.PaymentNotificationRequest;
import com.example.ecommerce.payment.dto.PaymentRequest;
import com.example.ecommerce.payment.mapper.PaymentMapper;
import com.example.ecommerce.payment.repository.PaymentRepository;
//import com.example.ecommerce.paypal.service.PaypalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final NotificationProducer notificationProducer;


    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, PaymentMapper paymentMapper, NotificationProducer notificationProducer) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.notificationProducer = notificationProducer;
    }

    public Integer createPayment(PaymentRequest paymentRequest) {
        var payment = paymentMapper.toPayment(paymentRequest);
        System.out.println("customer details" + paymentRequest.customer());
        // Create PayPal payment
//        String paymentUrl = paypalService.createPayPalPayment(paymentRequest.amount(), "USD", "Order payment");
        paymentRepository.save(payment);
        System.out.println("payment created");
        // produce a record to Kafka broker
        notificationProducer.sendNotification(
                new PaymentNotificationRequest(
                        paymentRequest.orderReference(),
                        paymentRequest.amount(),
                        paymentRequest.paymentMethod(),
                        paymentRequest.customer().firstName(),
                        paymentRequest.customer().lastName(),
                        paymentRequest.customer().email()
                )
        );

        // save new payment to database
        return payment.getId();
    }
}
