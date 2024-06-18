package com.oleksa.ecommerce.service;

import com.oleksa.ecommerce.dto.OrderDto;
import com.oleksa.ecommerce.dto.request.PaymentInfoRequest;
import com.oleksa.ecommerce.dto.request.PurchaseRequest;
import com.oleksa.ecommerce.dto.response.OrderDetailsResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderDto createOrder(String username, PurchaseRequest purchase);

    OrderDto fetchOrderById(String username, Long orderId);

    OrderDto fetchOrderByTrackingNumber(String username, String orderTrackingNumber);

    Page<OrderDto> fetchOrdersByEmail(String email, Pageable pageable);

    OrderDetailsResponse fetchOrderDetails(String username, Long orderId);

    PaymentIntent createPaymentIntent(PaymentInfoRequest paymentInfo) throws StripeException;

}
