package com.example.ecommerce.kafka.producer;

import com.example.ecommerce.config.KafkaOrderTopicConfig;
import com.example.ecommerce.kafka.dto.OrderConfirmation;
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
public class OrderProducer {
    private final KafkaTemplate<String, OrderConfirmation> kafkaTemplate;
    private final KafkaOrderTopicConfig kafkaOrderTopicConfig;

    public void sendOrderConfirmation(OrderConfirmation orderConfirmation){
        log.info("Sending order confirmation: {}", orderConfirmation);
        Message<OrderConfirmation> message = MessageBuilder
                .withPayload(orderConfirmation)
                .setHeader(KafkaHeaders.TOPIC, kafkaOrderTopicConfig.orderTopic().name())
                .build();
        kafkaTemplate.send(message);
    }
}
