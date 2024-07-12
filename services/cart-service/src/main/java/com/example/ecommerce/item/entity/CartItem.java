package com.example.ecommerce.item.entity;

import com.example.ecommerce.cart.entity.Cart;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer productId;

    private String productName;

    private String productDescription;

    private BigDecimal productPrice;

    @Column(nullable = false)
    private double quantity;

    @ManyToOne
    @JoinColumn(name="cart_id")
    private Cart cartId;
}
