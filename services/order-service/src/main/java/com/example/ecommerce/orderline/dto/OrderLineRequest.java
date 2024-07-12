package com.example.ecommerce.orderline.dto;

public record OrderLineRequest (
        Integer orderId,
        Integer productId,
        double quantity
){

}
