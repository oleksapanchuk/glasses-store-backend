package com.oleksa.ecommerce.controller;

import com.oleksa.ecommerce.dto.request.PaymentInfoRequest;
import com.oleksa.ecommerce.dto.PurchaseResponse;
import com.oleksa.ecommerce.dto.request.PurchaseRequest;
import com.oleksa.ecommerce.service.CheckoutService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/checkout")
@Log4j2
public class CheckoutController {

    private final CheckoutService checkoutService;

    @Autowired
    public CheckoutController(
            CheckoutService checkoutService
    ) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/purchase")
    public PurchaseResponse placeOrder(
            @RequestBody PurchaseRequest purchase
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return checkoutService.placeOrder(username, purchase);
    }

    @PostMapping("/payment-intent")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentInfoRequest paymentInfo) throws StripeException {

        log.info("PaymentInfoRequest.amount: {}", paymentInfo.getAmount());

        PaymentIntent paymentIntent = checkoutService.createPaymentIntent(paymentInfo);

        return ResponseEntity.ok(paymentIntent.toJson());
    }
}
