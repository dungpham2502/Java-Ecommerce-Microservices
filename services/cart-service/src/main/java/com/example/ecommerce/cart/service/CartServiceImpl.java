package com.example.ecommerce.cart.service;

import com.example.ecommerce.cart.dto.CartRequest;
import com.example.ecommerce.cart.dto.CartResponse;
import com.example.ecommerce.cart.mapper.CartMapper;
import com.example.ecommerce.cart.repository.CartRepository;
import com.example.ecommerce.customer.client.CustomerClient;
import com.example.ecommerce.customer.dto.CustomerResponse;
import com.example.ecommerce.exception.BusinessException;
import com.example.ecommerce.item.dto.CartItemRequest;
import com.example.ecommerce.item.entity.CartItem;
import com.example.ecommerce.product.client.ProductClient;
import com.example.ecommerce.product.dto.ProductRequest;
import com.example.ecommerce.product.dto.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final CartMapper cartMapper;

    @Autowired
    public CartServiceImpl(CustomerClient customerClient, ProductClient productClient, CartMapper cartMapper, CartRepository cartRepository, CustomerClient customerClient) {
        this.customerClient = customerClient;
        this.productClient = productClient;
        this.cartMapper = cartMapper;
        this.cartRepository = cartRepository;
    }


    @Override
    public Integer createCart(CartRequest request) {
        // check if customer exists
        var customer = customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException("Cannot add to cart:: No customer exists"));

        var existingCart = cartRepository.findByCustomerId(request.customerId());
        if(existingCart.isPresent()) {
            throw new BusinessException("Cannot add to cart:: Customer already exists");
        }
        // check item quantity
        var productIds = request.items().stream()
                .map(CartItemRequest::productId).toList();

        var quantities = request.items().stream()
                .map(CartItemRequest::quantity).toList();

        List<ProductResponse> availableItems = productClient.checkAvailability(productIds, quantities);
        if (availableItems.isEmpty() || availableItems.size() != request.items().size()) {
            throw new BusinessException("Cannot add to cart:: One or more products are not available in the requested quantity");
        }


        var cart = cartMapper.toCart(request, availableItems);
        return cartRepository.save(cart).getId();

    }

    @Override
    public void addItemToCart(Integer cartId, CartItemRequest cartItemRequest) {
        var cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new BusinessException("Cannot add to cart:: No cart exists"));

        // Check if the item already exists in the cart
        Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(cartItemRequest.productId()))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            double newQuantity = existingItem.getQuantity() + cartItemRequest.quantity();

            // Check item quantity
            List<ProductResponse> availableItems = productClient.checkAvailability(
                    List.of(cartItemRequest.productId()), List.of(newQuantity));
            if (availableItems.isEmpty()) {
                throw new BusinessException("Cannot add to cart:: Product is not available in the requested quantity");
            }

            // Update the existing item's quantity
            existingItem.setQuantity(newQuantity);
            existingItem.setProductName(availableItems.get(0).name());
            existingItem.setProductDescription(availableItems.get(0).description());
            existingItem.setProductPrice(availableItems.get(0).price());
        } else {
            // Check item quantity
            List<ProductResponse> availableItems = productClient.checkAvailability(
                    List.of(cartItemRequest.productId()), List.of(cartItemRequest.quantity()));
            if (availableItems.isEmpty()) {
                throw new BusinessException("Cannot add to cart:: Product is not available in the requested quantity");
            }

            // Add new item to the cart
            CartItem cartItem = cartMapper.toCartItem(cartItemRequest);
            cartItem.setProductName(availableItems.get(0).name());
            cartItem.setProductDescription(availableItems.get(0).description());
            cartItem.setProductPrice(availableItems.get(0).price());
            cartItem.setCartId(cart);  // Ensure the cart relationship is set
            cart.getItems().add(cartItem);
        }

        // Save the cart with the updated items
        cartRepository.save(cart);
    }


    @Override
    public CartResponse getCart(Integer customerId) {
        var cart = cartRepository.findByCustomerId(customerId).orElseThrow(() -> new BusinessException("Cannot get cart:: No cart exists"));
        return cartMapper.toCartResponse(cart);
    }

    @Override
    public void removeFromCart(Integer customerId, Integer productId) {
        var cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new BusinessException("No cart found for customer ID:: " + customerId));
        cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        cartRepository.save(cart);
    }

    @Override
    public void clearCart(Integer customerId) {
        var cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new BusinessException("No cart found for customer ID:: " + customerId));
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public Optional<CustomerResponse> getCustomer(Integer customerId){
        return Optional.ofNullable(customerClient.findCustomerById(customerId)
                .orElseThrow(() -> new BusinessException("Cannot add to cart:: No customer exists")));
    }
}
