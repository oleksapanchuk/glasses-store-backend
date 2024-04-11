package com.oleksa.ecommerce.service;

import com.oleksa.ecommerce.dto.OrderDto;
import com.oleksa.ecommerce.entity.add.OrderStatus;
import org.springframework.data.domain.Page;

public interface AdminService {

    Page<OrderDto> fetchOrdersByTrackingNumber(String trackingNumber);

    boolean updateOrderStatus(Long orderId, String orderStatus);
}
