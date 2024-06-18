package com.oleksa.ecommerce.dto.request;

import com.oleksa.ecommerce.dto.AddressDto;
import com.oleksa.ecommerce.dto.OrderDto;
import com.oleksa.ecommerce.entity.OrderItem;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class PurchaseRequest {
    private Long userId;
    private AddressDto address;
    private Set<OrderItem> orderItems;
}
