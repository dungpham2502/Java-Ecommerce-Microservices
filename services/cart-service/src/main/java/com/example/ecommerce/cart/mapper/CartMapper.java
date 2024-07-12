package com.example.ecommerce.cart.mapper;

import com.example.ecommerce.cart.dto.CartRequest;
import com.example.ecommerce.cart.dto.CartResponse;
import com.example.ecommerce.cart.entity.Cart;
import com.example.ecommerce.exception.BusinessException;
import com.example.ecommerce.item.dto.CartItemRequest;
import com.example.ecommerce.item.dto.CartItemResponse;
import com.example.ecommerce.item.entity.CartItem;
import com.example.ecommerce.product.dto.ProductResponse;
import lombok.Builder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Component
public class CartMapper {
    public Cart toCart(CartRequest request, List<ProductResponse> productDetails) {
        Cart cart = new Cart();
        Map<Integer, CartItemRequest> itemRequestMap = request.items().stream()
                .collect(toMap(CartItemRequest::productId, item -> item));

        // Map productDetails to CartItems
        cart.setCustomerId(request.customerId());
        cart.setItems(productDetails.stream().map(
                product -> {
                    CartItemRequest cartItemRequest = itemRequestMap.get(product.productId());
                    if (cartItemRequest == null) {
                        throw new BusinessException("Product not found in cart request");
                    }
                    return new CartItem(
                            null,
                            product.productId(),
                            product.name(),
                            product.description(),
                            product.price(),
                            cartItemRequest.quantity(),  // Use quantity from CartItemRequest
                            cart
                    );
                }
        ).collect(toList()));

        return cart;
    }

    public CartResponse toCartResponse(Cart cart) {
        return new CartResponse(
                cart.getId(),
                cart.getCustomerId(),
                cart.getItems().stream()
                        .map(this::toCartItemResponse)
                        .toList()
        );
    }


    public CartItem toCartItem(CartItemRequest itemRequest) {
        return new CartItem(
                null,
                itemRequest.productId(),
                null,
                null,
                null,
                itemRequest.quantity(),
                itemRequest.cartId()
        );
    }

    private CartItemResponse toCartItemResponse(CartItem item) {
        return new CartItemResponse(
                item.getProductId(),
                item.getProductName(),
                item.getProductDescription(),
                item.getProductPrice(),
                item.getQuantity()
        );
    }
}
