package com.example.ecommerce.order.entity;

import com.example.ecommerce.payment.entity.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "order_confirmations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderConfirmation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String orderReference;
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private String customerFirstname;
    private String customerLastname;
    private String customerEmail;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_confirmation_id")
    private List<Product> products;
}
