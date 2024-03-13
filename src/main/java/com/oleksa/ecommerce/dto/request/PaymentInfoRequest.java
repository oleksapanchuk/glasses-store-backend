package com.oleksa.ecommerce.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentInfoRequest {
    private int amount;
    private String currency;
    private String receiptEmail;
}