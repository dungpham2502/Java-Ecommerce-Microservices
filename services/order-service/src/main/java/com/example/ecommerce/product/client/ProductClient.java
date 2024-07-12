package com.example.ecommerce.product.client;

import com.example.ecommerce.exception.BusinessException;
import com.example.ecommerce.product.dto.ProductAvailabilityResponse;
import com.example.ecommerce.product.dto.PurchaseRequest;
import com.example.ecommerce.product.dto.PurchaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import java.util.List;

@FeignClient(name = "product-service", url = "${application.config.product-url}")
public interface ProductClient {

    @PostMapping(value = "/purchase", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<PurchaseResponse> purchaseProducts(@RequestBody List<PurchaseRequest> requestBody);

    @GetMapping("/check-availability")
    List<ProductAvailabilityResponse> checkAvailability(
            @RequestParam("productIds") List<Integer> productIds,
            @RequestParam("quantities") List<Double> quantities
    );
}
