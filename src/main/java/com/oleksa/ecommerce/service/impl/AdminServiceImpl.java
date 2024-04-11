package com.oleksa.ecommerce.service.impl;

import com.oleksa.ecommerce.dto.OrderDto;
import com.oleksa.ecommerce.entity.Order;
import com.oleksa.ecommerce.exception.ResourceNotFoundException;
import com.oleksa.ecommerce.mapper.OrderMapper;
import com.oleksa.ecommerce.repository.OrderRepository;
import com.oleksa.ecommerce.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {

    private final OrderRepository orderRepository;

    @Override
    public Page<OrderDto> fetchOrdersByTrackingNumber(String trackingNumber) {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());

        return orderRepository.findByTrackingNumber(trackingNumber, pageable)
                .map(OrderMapper::mapToOrderDto);
    }

    @Override
    public boolean updateOrderStatus(Long orderId, String orderStatus) {

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order", "orderId", orderId)
        );

        order.setStatus(orderStatus);

        orderRepository.save(order);

        return true;
    }
}
