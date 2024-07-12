package com.example.ecommerce.orderline.mapper;

import com.example.ecommerce.order.entity.Order;
import com.example.ecommerce.orderline.entity.OrderLine;
import com.example.ecommerce.orderline.dto.OrderLineRequest;
import com.example.ecommerce.orderline.dto.OrderLineResponse;
import org.springframework.stereotype.Service;

@Service
public class OrderLineMapper {
    public OrderLine toOrderLine(OrderLineRequest request) {
        return OrderLine.builder()
                .quantity(request.quantity())
                .order(Order.builder().id(request.orderId()).build())
                .productId(request.productId())
                .build();
    }

    public OrderLineResponse toOrderLineResponse(OrderLine orderLine) {
        return new OrderLineResponse(orderLine.getId(), orderLine.getQuantity());
    }
}
