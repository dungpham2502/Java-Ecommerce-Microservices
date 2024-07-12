package com.example.ecommerce.orderline.controller;


import com.example.ecommerce.orderline.dto.OrderLineResponse;
import com.example.ecommerce.orderline.service.OrderLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-lines")
public class OrderLineController {
    private final OrderLineService orderLineService;

    @Autowired
    public OrderLineController(OrderLineService orderLineService) {
        this.orderLineService = orderLineService;
    }

    @GetMapping("/order/{order-id}")
    public ResponseEntity <List<OrderLineResponse>> getOrderLineById(@PathVariable("order-id") Integer orderId) {
        return ResponseEntity.ok(orderLineService.findAllByOrderId(orderId));
    }
}
