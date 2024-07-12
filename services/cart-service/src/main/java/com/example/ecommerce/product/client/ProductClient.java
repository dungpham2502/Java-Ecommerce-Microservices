package com.example.ecommerce.product.client;

import com.example.ecommerce.item.dto.CartItemRequest;
import com.example.ecommerce.product.dto.ProductDetailsResponse;
import com.example.ecommerce.product.dto.ProductRequest;
import com.example.ecommerce.product.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name= "product-service",
        url= "${application.config.product-url}"
)
public interface ProductClient {
    @GetMapping("/check-availability")
    List<ProductResponse> checkAvailability(@RequestParam("productIds") List<Integer> productIds, @RequestParam("quantities") List<Double> quantities);
}
