package com.oleksa.ecommerce.mapper;

import com.oleksa.ecommerce.dto.OrderDto;
import com.oleksa.ecommerce.entity.Order;

public class OrderMapper {

    public static OrderDto mapToOrderDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .orderTrackingNumber(order.getOrderTrackingNumber())
                .totalQuantity(order.getTotalQuantity())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .dateCreated(order.getDateCreated())
                .build();
    }

    public static Order mapToOrder(OrderDto orderDto) {
        return Order.builder()
                .orderTrackingNumber(orderDto.getOrderTrackingNumber())
                .totalQuantity(orderDto.getTotalQuantity())
                .totalPrice(orderDto.getTotalPrice())
                .status(orderDto.getStatus())
                .build();
    }
}