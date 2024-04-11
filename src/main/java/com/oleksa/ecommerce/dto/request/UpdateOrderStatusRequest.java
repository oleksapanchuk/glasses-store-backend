package com.oleksa.ecommerce.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateOrderStatusRequest {
    private Long orderId;
    private String orderStatus;
}
