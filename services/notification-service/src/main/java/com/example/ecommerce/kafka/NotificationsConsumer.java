package com.example.ecommerce.kafka;

import com.example.ecommerce.email.service.EmailService;
import com.example.ecommerce.order.entity.OrderConfirmation;
import com.example.ecommerce.payment.entity.PaymentConfirmation;
import com.example.ecommerce.notification.entity.Notification;
import com.example.ecommerce.notification.repository.NotificationRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.example.ecommerce.notification.entity.NotificationType.ORDER_CONFIRMATION;
import static com.example.ecommerce.notification.entity.NotificationType.PAYMENT_CONFIRMATION;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationsConsumer {

    private final NotificationRepository repository;
//    private final EmailService emailService;

    @KafkaListener(topics = "payment-topic")
    public void consumePaymentSuccessNotifications(PaymentConfirmation paymentConfirmation) throws MessagingException {
        log.info(format("Consuming the message from payment-topic Topic:: %s", paymentConfirmation));
        savePaymentNotification(paymentConfirmation);
//        sendPaymentSuccessEmail(paymentConfirmation);
    }

    @KafkaListener(topics = "order-topic")
    public void consumeOrderConfirmationNotifications(OrderConfirmation orderConfirmation) throws MessagingException {
        log.info(format("Consuming the message from order-topic Topic:: %s", orderConfirmation));
        saveOrderNotification(orderConfirmation);
//        sendOrderConfirmationEmail(orderConfirmation);
    }

    private void savePaymentNotification(PaymentConfirmation paymentConfirmation) {
        repository.save(
                Notification.builder()
                        .type(PAYMENT_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .paymentConfirmation(paymentConfirmation)
                        .build()
        );
    }

//    private void sendPaymentSuccessEmail(PaymentConfirmation paymentConfirmation) throws MessagingException {
//        var customerName = paymentConfirmation.getCustomerFirstname() + " " + paymentConfirmation.getCustomerLastname();
//        emailService.sendPaymentSuccessEmail(
//                paymentConfirmation.getCustomerEmail(),
//                customerName,
//                paymentConfirmation.getAmount(),
//                paymentConfirmation.getOrderReference()
//        );
//    }

    private void saveOrderNotification(OrderConfirmation orderConfirmation) {
         orderConfirmation.getProducts().forEach(product -> product.setOrderConfirmation(orderConfirmation));
         repository.save(
                Notification.builder()
                        .type(ORDER_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .orderConfirmation(orderConfirmation)
                        .build()
        );
    }

//    private void sendOrderConfirmationEmail(OrderConfirmation orderConfirmation) throws MessagingException {
//        var customerName = orderConfirmation.getCustomerFirstname() + " " + orderConfirmation.getCustomerLastname();
//        emailService.sendOrderConfirmationEmail(
//                orderConfirmation.getCustomerEmail(),
//                customerName,
//                orderConfirmation.getTotalAmount(),
//                orderConfirmation.getOrderReference(),
//                orderConfirmation.getProducts()
//        );
//    }
}
