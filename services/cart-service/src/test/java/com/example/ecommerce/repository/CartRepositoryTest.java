package com.example.ecommerce.repository;

import com.example.ecommerce.cart.entity.Cart;
import com.example.ecommerce.cart.repository.CartRepository;
import com.example.ecommerce.item.entity.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = {
        "application.config.customer-url=http://localhost:0000",
        "application.config.product-url=http://localhost:0000"
})
public class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    private Cart cart;

    @BeforeEach
    public void setUp() {
        cart = Cart.builder()
                .customerId(1)
                .items(List.of(
                        CartItem.builder()
                                .productId(1)
                                .productName("Product Name")
                                .productDescription("Product Description")
                                .productPrice(BigDecimal.valueOf(100))
                                .quantity(2.0)
                                .build()))
                .build();
        cart = cartRepository.save(cart);
    }



    @Test
    public void CartRepository_FindByCustomerId_ReturnsCart() {
        Optional<Cart> foundCart = cartRepository.findByCustomerId(1);

        assertTrue(foundCart.isPresent());
        assertEquals(cart.getId(), foundCart.get().getId());
        assertEquals(1, foundCart.get().getCustomerId());
    }

    @Test
    public void CartRepository_SaveCart_ReturnsCart() {
        Cart newCart = Cart.builder()
                .customerId(2)
                .items(List.of(
                        CartItem.builder()
                                .productId(2)
                                .productName("Another Product")
                                .productDescription("Another Description")
                                .productPrice(BigDecimal.valueOf(200))
                                .quantity(3.0)
                                .build()))
                .build();

        Cart savedCart = cartRepository.save(newCart);

        assertEquals(savedCart.getCustomerId(), newCart.getCustomerId());
        assertTrue(savedCart.getId() > 0);
    }

    @Test
    public void CartRepository_DeleteCart_ReturnsCart() {
        int cart_id = cart.getId();

        cartRepository.delete(cart);

        Optional<Cart> foundCart = cartRepository.findById(cart_id);
        assertFalse(foundCart.isPresent());
    }
}
