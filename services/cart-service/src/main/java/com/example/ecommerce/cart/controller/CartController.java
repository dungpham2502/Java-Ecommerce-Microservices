package com.example.ecommerce.cart.controller;


import com.example.ecommerce.cart.dto.CartRequest;
import com.example.ecommerce.cart.dto.CartResponse;
import com.example.ecommerce.cart.service.CartService;
import com.example.ecommerce.item.dto.CartItemRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/create")
    public ResponseEntity<Integer> createCart(@RequestBody CartRequest cartRequest) {
        Integer cartId = cartService.createCart(cartRequest);
        return new ResponseEntity<>(cartId, HttpStatus.CREATED);
    }

    @PutMapping("/add/{cartId}")
    public ResponseEntity<Void> addToCart(@PathVariable Integer cartId, @RequestBody CartItemRequest itemRequest) {
        cartService.addItemToCart(cartId, itemRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable Integer customerId) {
        CartResponse cartResponse = cartService.getCart(customerId);
        return ResponseEntity.ok(cartResponse);
    }

    @DeleteMapping("/remove/{customerId}/{productId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Integer customerId, @PathVariable Integer productId) {
        cartService.removeFromCart(customerId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear/{customerId}")
    public ResponseEntity<Void> clearCart(@PathVariable Integer customerId) {
        cartService.clearCart(customerId);
        return ResponseEntity.noContent().build();
    }
}
