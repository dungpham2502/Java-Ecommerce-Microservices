package com.example.ecommerce.cart.client;


import com.example.ecommerce.cart.dto.CartResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@FeignClient(
        name = "cart-service",
        url = "${application.config.cart-url}"
)
public interface CartClient {
    @GetMapping("/{customer-id}")
    CartResponse getCart(@PathVariable("customer-id") Integer customerId);

    @DeleteMapping("/clear/{customerId}")
    void clearCart(@PathVariable("customerId") Integer customerId);
}
