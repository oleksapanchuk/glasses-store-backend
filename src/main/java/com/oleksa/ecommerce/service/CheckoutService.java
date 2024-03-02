package com.oleksa.ecommerce.service;

import com.oleksa.ecommerce.dto.PaymentInfo;
import com.oleksa.ecommerce.dto.Purchase;
import com.oleksa.ecommerce.dto.PurchaseResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface CheckoutService {

    PurchaseResponse placeOrder(Purchase purchase);

    PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException;

}
