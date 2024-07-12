package com.example.ecommerce.notification.kafka;

import com.example.ecommerce.config.KafkaPaymentTopicConfig;
import com.example.ecommerce.notification.dto.PaymentNotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProducer {
    private final KafkaTemplate<String, PaymentNotificationRequest> kafkaTemplate;
    private final KafkaPaymentTopicConfig kafkaPaymentTopicConfig;

    public void sendNotification(PaymentNotificationRequest paymentNotificationRequest) {
        log.info("Sending payment notification request: {}", paymentNotificationRequest);
        Message<PaymentNotificationRequest> message = MessageBuilder
                .withPayload(paymentNotificationRequest)
                .setHeader(KafkaHeaders.TOPIC, kafkaPaymentTopicConfig.paymentTopic().name())
                .build();
        kafkaTemplate.send(message);
    }
}
