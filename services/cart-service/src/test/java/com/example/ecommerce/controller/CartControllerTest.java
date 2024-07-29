package com.example.ecommerce.controller;


import com.example.ecommerce.cart.controller.CartController;
import com.example.ecommerce.cart.dto.CartRequest;
import com.example.ecommerce.cart.dto.CartResponse;
import com.example.ecommerce.cart.entity.Cart;
import com.example.ecommerce.cart.service.CartService;
import com.example.ecommerce.customer.client.CustomerClient;
import com.example.ecommerce.customer.dto.CustomerResponse;
import com.example.ecommerce.item.dto.CartItemRequest;
import com.example.ecommerce.item.dto.CartItemResponse;
import com.example.ecommerce.product.client.ProductClient;
import com.example.ecommerce.product.dto.ProductResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.postgresql.hostchooser.HostRequirement.any;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(controllers = CartController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "application.config.customer-url=http://localhost:0000",
        "application.config.product-url=http://localhost:0000"
})
public class CartControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @MockBean
    private CustomerClient customerClient;

    @MockBean
    private ProductClient productClient;

    @Autowired
    private ObjectMapper objectMapper;

    private CartRequest cartRequest;
    private CartResponse cartResponse;
    private CartItemRequest cartItemRequest;
    private CartItemResponse cartItemResponse;
    private Integer customerId;
    private Integer cartId;
    private Integer productId;
    private double quantity;
    private BigDecimal price;
    private Cart cart;

    @BeforeEach
    public void setup() {
        customerId = 1;
        cartId = 1;
        productId = 1;
        quantity = 2.0;
        price = BigDecimal.valueOf(100);

        cart = Cart.builder()
                .id(cartId)
                .customerId(customerId)
                .items(new ArrayList<>())
                .build();

        cartItemRequest = new CartItemRequest(productId, quantity, cart);
        cartRequest = new CartRequest(customerId, List.of(cartItemRequest));

        cartItemResponse = new CartItemResponse(productId, "Product Name", "Product Description", price, quantity);
        cartResponse = new CartResponse(cartId, customerId, List.of(cartItemResponse));

        // Mock responses for Feign clients
        when(customerClient.findCustomerById(customerId)).thenReturn(Optional.of(new CustomerResponse("John", "Doe", "john.doe@example.com")));
        when(productClient.checkAvailability(any(List.class), any(List.class))).thenReturn(List.of(new ProductResponse(productId, "Product Name", "Product Description", price, quantity)));
    }



    @Test
    public void CartController_CreateCart_Success() throws Exception {
        // Arrange
        when(cartService.createCart(any(CartRequest.class))).thenReturn(cartId);

        // Act & Assert
        mockMvc.perform(post("/api/v1/cart/create")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(cartRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(cartId));
    }

    @Test
    public void CartController_GetCart_ReturnCart() throws Exception {
        // Arrange
        when(cartService.getCart(any(Integer.class))).thenReturn(cartResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/cart/{customer-id}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cartId))
                .andExpect(jsonPath("$.customerId").value(customerId))
                .andExpect(jsonPath("$.products[0].productId").value(1))
                .andExpect(jsonPath("$.products[0].name").value("Product Name"))
                .andExpect(jsonPath("$.products[0].description").value("Product Description"))
                .andExpect(jsonPath("$.products[0].price").value(BigDecimal.valueOf(100)))
                .andExpect(jsonPath("$.products[0].quantity").value(2));
    }

    @Test
    public void CartController_AddItemToCart_Success() throws Exception {
        // Arrange
        doNothing().when(cartService).addItemToCart(any(Integer.class), any(CartItemRequest.class));

        // Act & Assert
        mockMvc.perform(put("/api/v1/cart/add/{cartId}", cartId)
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(cartItemRequest)))
                .andExpect(status().isOk());
    }



    @Test
    public void CartController_RemoveFromCart_Success() throws Exception {
        // Arrange
        doNothing().when(cartService).removeFromCart(any(Integer.class), any(Integer.class));

        // Act & Assert
        mockMvc.perform(delete("/api/v1/cart/remove/{customerId}/{productId}", customerId, productId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void CartController_ClearCart_Success() throws Exception {
        // Arrange
        doNothing().when(cartService).clearCart(any(Integer.class));

        // Act & Assert
        mockMvc.perform(delete("/api/v1/cart/clear/{customerId}", customerId))
                .andExpect(status().isNoContent());
    }
}
