package com.oleksa.ecommerce.controller;

import com.oleksa.ecommerce.dto.PaymentInfo;
import com.oleksa.ecommerce.dto.Purchase;
import com.oleksa.ecommerce.dto.PurchaseResponse;
import com.oleksa.ecommerce.service.CheckoutService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestBody Purchase purchase
            ) {
        return checkoutService.placeOrder(purchase);
    }

    @PostMapping("/payment-intent")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentInfo paymentInfo) throws StripeException {

        log.info("PaymentInfo.amount: {}", paymentInfo.getAmount());

        PaymentIntent paymentIntent = checkoutService.createPaymentIntent(paymentInfo);

        return ResponseEntity.ok(paymentIntent.toJson());
    }
}
