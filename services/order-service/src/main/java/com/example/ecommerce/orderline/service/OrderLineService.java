package com.example.ecommerce.orderline.service;


import com.example.ecommerce.orderline.dto.OrderLineRequest;
import com.example.ecommerce.orderline.dto.OrderLineResponse;
import com.example.ecommerce.orderline.entity.OrderLine;
import com.example.ecommerce.orderline.mapper.OrderLineMapper;
import com.example.ecommerce.orderline.repository.OrderLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderLineService {
    private final OrderLineRepository orderLineRepository;
    private final OrderLineMapper orderLineMapper;


    @Autowired
    public OrderLineService(OrderLineRepository orderLineRepository, OrderLineMapper orderLineMapper) {
        this.orderLineRepository = orderLineRepository;
        this.orderLineMapper = orderLineMapper;
    }

    public void saveOrderLine(OrderLineRequest request) {
        OrderLine orderLine = orderLineMapper.toOrderLine(request);
        orderLineRepository.save(orderLine);
    }

    public List<OrderLineResponse> findAllByOrderId(Integer orderId) {
        List<OrderLine> orderLines = orderLineRepository.findAllByOrderId(orderId);
        return orderLines.stream().map(orderLineMapper::toOrderLineResponse).collect(Collectors.toList());
    }
}
