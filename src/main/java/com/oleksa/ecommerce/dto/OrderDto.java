package com.oleksa.ecommerce.dto;

import com.oleksa.ecommerce.entity.OrderItem;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class OrderDto {
    private Long id;
    private Long userId;
    private String orderTrackingNumber;
    private Integer totalQuantity;
    private Integer totalPrice;
    private String status;
}
