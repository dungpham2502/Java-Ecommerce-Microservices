package com.example.ecommerce.service;

import com.example.ecommerce.cart.dto.CartRequest;
import com.example.ecommerce.cart.dto.CartResponse;
import com.example.ecommerce.cart.entity.Cart;
import com.example.ecommerce.cart.mapper.CartMapper;
import com.example.ecommerce.cart.repository.CartRepository;
import com.example.ecommerce.cart.service.CartServiceImpl;
import com.example.ecommerce.customer.client.CustomerClient;
import com.example.ecommerce.customer.dto.CustomerResponse;
import com.example.ecommerce.exception.BusinessException;
import com.example.ecommerce.item.dto.CartItemRequest;
import com.example.ecommerce.item.dto.CartItemResponse;
import com.example.ecommerce.item.entity.CartItem;
import com.example.ecommerce.product.client.ProductClient;
import com.example.ecommerce.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CustomerClient customerClient;

    @Mock
    private ProductClient productClient;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartMapper cartMapper;

    @InjectMocks
    private CartServiceImpl cartService;

    private CartRequest cartRequest;
    private CartResponse cartResponse;
    private CartItemRequest cartItemRequest;
    private Integer customerId;
    private Integer productId;
    private double quantity;
    private Integer cartId;
    private Cart cart;
    private CustomerResponse mockCustomerResponse;
    private ProductResponse mockProductResponse;

    @BeforeEach
    public void setUp() {
        customerId = 1;
        productId = 1;
        quantity = 2.0;
        cartId = 1;

        mockCustomerResponse = new CustomerResponse("John", "Doe", "john.doe@example.com");
        mockProductResponse = new ProductResponse(productId, "Product Name", "Product Description", BigDecimal.valueOf(100), 3.0);

        cart = Cart.builder()
                .id(cartId)
                .customerId(customerId)
                .items(new ArrayList<>())
                .build();

        CartItem cartItem = CartItem.builder()
                .id(1)
                .productId(productId)
                .productName("Product Name")
                .productDescription("Product Description")
                .productPrice(BigDecimal.valueOf(100))
                .quantity(quantity)
                .cartId(null)
                .build();

        cart.getItems().add(cartItem);

        cartItemRequest = new CartItemRequest(productId, quantity, cart);
        cartRequest = new CartRequest(customerId, List.of(cartItemRequest));
        cartResponse = new CartResponse(cartId, customerId, List.of(new CartItemResponse(1, "Product Name", "Product Description",  BigDecimal.valueOf(100), 2.0)));
    }


    @Test
    public void CartService_GetCart_ReturnCartResponse(){
        // Arrange
        when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.of(cart));
        when(cartMapper.toCartResponse(any(Cart.class))).thenReturn(cartResponse);

        // Act
        CartResponse response = cartService.getCart(customerId);

        // Assert
        assertNotNull(response);
        assertEquals(response.id(), cartResponse.id());
        assertEquals(response.customerId(), cartResponse.customerId());
    }

    @Test
    public void CartService_CreateCart_ReturnCartId() {
        // Arrange
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());
        when(cartMapper.toCart(any(CartRequest.class), any(List.class))).thenReturn(cart);
        when(customerClient.findCustomerById(customerId)).thenReturn(Optional.of(mockCustomerResponse));
        when(productClient.checkAvailability(any(List.class), any(List.class))).thenReturn(List.of(mockProductResponse));

        // Act
        Integer cartId = cartService.createCart(cartRequest);

        // Assert
        assertNotNull(cartId);
        assertEquals(1, cartId);
    }

    @Test
    public void CartService_CreateCart_CustomerNotExist_ThrowBusinessException(){
        // Arrange
        when(customerClient.findCustomerById(customerId)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            cartService.createCart(cartRequest);
        });

        assertEquals("Cannot add to cart:: No customer exists", exception.getMessage());
    }

    @Test
    public void CartService_CreateCart_CartAlreadyExists_ThrowBusinessException(){
        // Arrange
        when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.of(cart));
        when(customerClient.findCustomerById(customerId)).thenReturn(Optional.of(mockCustomerResponse));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            cartService.createCart(cartRequest);
        });

        assertEquals("Cannot create cart:: Cart already exists", exception.getMessage());
    }

    @Test
    public void CartService_AddItem_Success() {
        // Arrange
        Cart cartWithNoItems = Cart.builder()
                .id(cartId)
                .customerId(customerId)
                .items(new ArrayList<>())
                .build();

        CartItem newCartItem = CartItem.builder()
                .productId(productId)
                .productName("Product Name")
                .productDescription("Product Description")
                .productPrice(BigDecimal.valueOf(100))
                .quantity(quantity)
                .cartId(cartWithNoItems)
                .build();

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cartWithNoItems));
        when(cartMapper.toCartItem(cartItemRequest)).thenReturn(newCartItem);
        when(cartRepository.save(any(Cart.class))).thenReturn(cartWithNoItems);
        when(productClient.checkAvailability(any(List.class), any(List.class))).thenReturn(List.of(mockProductResponse));

        // Act
        cartService.addItemToCart(cartId, cartItemRequest);

        // Assert
        assertEquals(1, cartWithNoItems.getItems().size());
        CartItem addedItem = cartWithNoItems.getItems().get(0);
        assertEquals(productId, addedItem.getProductId());
        assertEquals(quantity, addedItem.getQuantity());
        assertEquals("Product Name", addedItem.getProductName());
        assertEquals("Product Description", addedItem.getProductDescription());
        assertEquals(BigDecimal.valueOf(100), addedItem.getProductPrice());
    }

    @Test
    public void CartService_AddItem_CartNotFound_ThrowsBusinessException() {
        // Arrange
        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            cartService.addItemToCart(cartId, cartItemRequest);
        });

        assertEquals("Cannot add to cart:: No cart exists", exception.getMessage());
    }

    @Test
    public void CartService_AddItem_ProductNotAvailable_ThrowsBusinessException() {
        // Arrange
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(productClient.checkAvailability(any(List.class), any(List.class))).thenReturn(List.of());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            cartService.addItemToCart(cartId, cartItemRequest);
        });

        assertEquals("Cannot add to cart:: Product is not available in the requested quantity", exception.getMessage());
    }

    @Test
    public void CartService_ClearCart_Success(){
        when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.of(cart));

        cartService.clearCart(customerId);

        assertEquals(0, cart.getItems().size());
    }

    @Test
    public void CartService_ClearCart_CartNotFound_ThrowsBusinessException() {
        // Arrange
        when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            cartService.clearCart(customerId);
        });

        assertEquals("No cart found for customer ID:: " + customerId, exception.getMessage());
    }

    @Test
    public void CartService_RemoveItem_Success() {
        // Arrange
        when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.of(cart));

        // Act
        cartService.removeFromCart(customerId, productId);

        // Assert
        assertEquals(0, cart.getItems().size());
    }

    @Test
    public void CartService_RemoveItem_CartNotFound_ThrowsBusinessException() {
        // Arrange
        when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            cartService.removeFromCart(customerId, productId);
        });

        assertEquals("No cart found for customer ID:: " + customerId, exception.getMessage());
    }
}
