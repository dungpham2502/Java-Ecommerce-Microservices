//package com.example.ecommerce.repository;
//
//import com.example.ecommerce.cart.entity.Cart;
//import com.example.ecommerce.cart.repository.CartRepository;
//import com.example.ecommerce.cart.service.CartService;
//import com.example.ecommerce.item.entity.CartItem;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Optional;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
//public class CartRepositoryTest {
//
//    @Autowired
//    private CartRepository cartRepository;
//
//    private Cart cart;
//    private CartItem cartItem1;
//    private CartItem cartItem2;
//
//    @BeforeEach
//    public void setUp() {
//        cartRepository.deleteAll();
//
//        cartItem1 = CartItem.builder()
//                .productId(1)
//                .productName("Product 1")
//                .productDescription("Description 1")
//                .productPrice(BigDecimal.valueOf(100.0))
//                .quantity(1.0)
//                .build();
//
//        cartItem2 = CartItem.builder()
//                .productId(2)
//                .productName("Product 2")
//                .productDescription("Description 2")
//                .productPrice(BigDecimal.valueOf(200.0))
//                .quantity(2.0)
//                .build();
//
//        cart = Cart.builder()
//                .customerId(1)
//                .items(new ArrayList<>(Arrays.asList(cartItem1, cartItem2)))
//                .build();
//    }
//
//    @Test
//    public void CartRepository_Saved_ReturnSavedCart() {
//        Cart savedCart = cartRepository.save(cart);
//
//        Assertions.assertNotNull(savedCart);
//        Assertions.assertNotNull(savedCart.getId());
//        Assertions.assertEquals(cart.getCustomerId(), savedCart.getCustomerId());
//        Assertions.assertEquals(2, savedCart.getItems().size());
//    }
//
//}
