package com.example.ecommerce.notification.entity;

import com.example.ecommerce.order.entity.OrderConfirmation;
import com.example.ecommerce.payment.entity.PaymentConfirmation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private LocalDateTime notificationDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_confirmation_id")
    private OrderConfirmation orderConfirmation;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_confirmation_id")
    private PaymentConfirmation paymentConfirmation;
}
