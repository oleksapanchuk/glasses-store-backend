package com.oleksa.ecommerce.controller;

import com.oleksa.ecommerce.dto.OrderDto;
import com.oleksa.ecommerce.dto.request.PaymentInfoRequest;
import com.oleksa.ecommerce.dto.request.PurchaseRequest;
import com.oleksa.ecommerce.dto.response.OrderDetailsResponse;
import com.oleksa.ecommerce.service.OrderService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Log4j2
@AllArgsConstructor
@RequestMapping("/api/order")
@RestController
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/fetch-by-id/{order-id}")
    public ResponseEntity<OrderDto> fetchOrderById(
            @PathVariable(name = "order-id") Long orderId
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        OrderDto orderDto = orderService.fetchOrderById(username, orderId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderDto);
    }

    @GetMapping("/fetch-by-tracking-number/{order-tracking-number}")
    public ResponseEntity<OrderDto> fetchOrderByTrackingNumber(
            @PathVariable(name = "order-tracking-number") String orderTrackingNumber
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        OrderDto orderDto = orderService.fetchOrderByTrackingNumber(username, orderTrackingNumber);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderDto);
    }

    @GetMapping("/fetch-by-username")
    public ResponseEntity<Page<OrderDto>> fetchOrderByUsername(
            Pageable pageable
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Page<OrderDto> orderDtoPage = orderService.fetchOrdersByUsername(username, pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderDtoPage);
    }

    @GetMapping("/fetch-order-details/{order-id}")
    public ResponseEntity<OrderDetailsResponse> fetchOrderDetailsById(
            @PathVariable(name = "order-id") Long orderId
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        OrderDetailsResponse orderDetailsResponse = orderService.fetchOrderDetails(username, orderId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderDetailsResponse);
    }

    @PostMapping("/place-order")
    public ResponseEntity<OrderDto> placeOrder(
            @RequestBody PurchaseRequest purchase
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        OrderDto orderDto = orderService.createOrder(username, purchase);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderDto);
    }

    @PostMapping("/create-payment-intent")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentInfoRequest paymentInfo) throws StripeException {

        log.info("PaymentInfoRequest.amount: {}", paymentInfo.getAmount());

        PaymentIntent paymentIntent = orderService.createPaymentIntent(paymentInfo);

        return ResponseEntity.ok(paymentIntent.toJson());
    }
}
