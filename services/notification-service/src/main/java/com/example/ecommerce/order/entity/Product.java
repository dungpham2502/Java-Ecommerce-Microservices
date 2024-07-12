package com.example.ecommerce.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer productId;
    private String name;
    private String description;
    private BigDecimal price;
    private double quantity;

    @ManyToOne
    @JoinColumn(name = "order_confirmation_id", nullable = false)
    private OrderConfirmation orderConfirmation;

}
