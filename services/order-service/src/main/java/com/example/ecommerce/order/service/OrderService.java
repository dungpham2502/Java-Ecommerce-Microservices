package com.example.ecommerce.order.service;

import com.example.ecommerce.order.dto.OrderRequest;
import com.example.ecommerce.order.dto.OrderResponse;
import com.example.ecommerce.order.entity.PaymentMethod;

import java.util.List;

public interface OrderService {
    Integer createOrder(OrderRequest orderRequest);
    List<OrderResponse> findAll();
    OrderResponse findById(Integer id);
    List<OrderResponse> findOrdersByCustomerId(Integer customerId);
}
