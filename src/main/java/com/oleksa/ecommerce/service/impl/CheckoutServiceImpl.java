package com.oleksa.ecommerce.service.impl;

import com.oleksa.ecommerce.repository.UserRepository;
import com.oleksa.ecommerce.dto.PaymentInfo;
import com.oleksa.ecommerce.dto.Purchase;
import com.oleksa.ecommerce.dto.PurchaseResponse;
import com.oleksa.ecommerce.entity.User;
import com.oleksa.ecommerce.entity.Order;
import com.oleksa.ecommerce.entity.OrderItem;
import com.oleksa.ecommerce.service.CheckoutService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j2
public class CheckoutServiceImpl implements CheckoutService {

    private final UserRepository customerRepository;

    @Autowired
    public CheckoutServiceImpl(
            UserRepository customerRepository,
            @Value("${stripe.keys.secret}") String secretKey
    ) {
        this.customerRepository = customerRepository;

        // initialize Stripe API with secret key
        Stripe.apiKey = secretKey;
    }

    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {

        // retrieve the order info from dto
        Order order = purchase.getOrder();

        // generate tracking number
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        // populate order with orderItems
        Set<OrderItem> orderItems = purchase.getOrderItems();
        orderItems.forEach(order::add);

        // populate order with billingAddress and shippingAddress
        order.setShippingAddress(purchase.getShippingAddress());

        // populate users with order
        User users = purchase.getUsers();

        // check if this is an existing users
        String theEmail = users.getEmail();
        User usersFromDB = customerRepository.findByEmail(theEmail).orElseThrow();

        if (usersFromDB != null) {
            // we found them ... let's assign them accordingly
            users = usersFromDB;
        }

        users.add(order);

        // save to the database
        customerRepository.save(users);

        // return a response
        return new PurchaseResponse(orderTrackingNumber);
    }

    @Override
    public PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException {

        List<String> paymentMethodTypes = List.of("card");

        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentInfo.getAmount());
        params.put("currency", paymentInfo.getCurrency());
        params.put("payment_method_types", paymentMethodTypes);
        params.put("description", "PanShop Purchase");
        params.put("receipt_email", paymentInfo.getReceiptEmail());

        return PaymentIntent.create(params);
    }

    private String generateOrderTrackingNumber() {

        // generate a random UUID number (UUID version-4)
        // For details see: https://en.wikipedia.org/wiki/Universally_unique_identifier#Version_4_(random)

        return UUID.randomUUID().toString();
    }

}
