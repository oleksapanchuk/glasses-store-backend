package com.oleksa.ecommerce.service;

import com.oleksa.ecommerce.dto.request.PaymentInfoRequest;
import com.oleksa.ecommerce.dto.request.PurchaseRequest;
import com.oleksa.ecommerce.dto.PurchaseResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface CheckoutService {

    PurchaseResponse placeOrder(String username, PurchaseRequest purchase);

    PaymentIntent createPaymentIntent(PaymentInfoRequest paymentInfo) throws StripeException;

}
