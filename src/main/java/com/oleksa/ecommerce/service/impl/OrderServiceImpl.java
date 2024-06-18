package com.oleksa.ecommerce.service.impl;

import com.oleksa.ecommerce.dto.OrderDto;
import com.oleksa.ecommerce.dto.ProductDto;
import com.oleksa.ecommerce.dto.request.PaymentInfoRequest;
import com.oleksa.ecommerce.dto.request.PurchaseRequest;
import com.oleksa.ecommerce.dto.response.OrderDetailsResponse;
import com.oleksa.ecommerce.dto.response.OrderItemResponse;
import com.oleksa.ecommerce.entity.*;
import com.oleksa.ecommerce.entity.add.OrderStatus;
import com.oleksa.ecommerce.exception.ResourceNotFoundException;
import com.oleksa.ecommerce.exception.UnauthorizedAccessException;
import com.oleksa.ecommerce.mapper.AddressMapper;
import com.oleksa.ecommerce.mapper.OrderMapper;
import com.oleksa.ecommerce.repository.OrderRepository;
import com.oleksa.ecommerce.repository.ProductRepository;
import com.oleksa.ecommerce.repository.UserRepository;
import com.oleksa.ecommerce.service.OrderService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.oleksa.ecommerce.mapper.OrderMapper.mapToOrderDto;
import static com.oleksa.ecommerce.mapper.ProductMapper.mapToProductDto;

@Log4j2
@Service
public class OrderServiceImpl implements OrderService {
    private final ProductRepository productRepository;

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(
            UserRepository customerRepository,
            OrderRepository orderRepository,
            @Value("${stripe.keys.secret}") String secretKey,
            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = customerRepository;
        this.productRepository = productRepository;

        // initialize Stripe API with secret key
        Stripe.apiKey = secretKey;
    }

    @Override
    @Transactional
    public OrderDto createOrder(String email, PurchaseRequest purchase) {

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User", "email", email)
        );

        if (purchase == null) {
            throw new IllegalArgumentException("Purchase request is null");
        }

        Order order = OrderMapper.mapToOrder(purchase.getOrder());
        order.setOrderTrackingNumber(generateOrderTrackingNumber());
        order.setStatus(OrderStatus.ACCEPTED.name());
        Set<OrderItem> orderItems = purchase.getOrderItems();
        orderItems.forEach(order::add);
        Address shippingAddress = AddressMapper.mapToAddress(purchase.getShippingAddress());
        shippingAddress.setUser(user);
        order.setShippingAddress(shippingAddress);
        order.setUser(user);
        orderRepository.save(order);

        return mapToOrderDto(order);
    }

    @Override
    public OrderDto fetchOrderById(String email, Long orderId) {

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User", "email", email)
        );

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order", "id", orderId)
        );

        if ((long) order.getUser().getId() != user.getId()) {
            throw new UnauthorizedAccessException("order", "id", orderId);
        }

        return mapToOrderDto(order);
    }

    @Override
    public OrderDto fetchOrderByTrackingNumber(String email, String orderTrackingNumber) {

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User", "email", email)
        );

        Order order = orderRepository.findByOrderTrackingNumber(orderTrackingNumber).orElseThrow(
                () -> new ResourceNotFoundException("Order", "tracking number", orderTrackingNumber)
        );

        if ((long) order.getUser().getId() != user.getId()) {
            throw new UnauthorizedAccessException("order", "tracking number", orderTrackingNumber);
        }

        return mapToOrderDto(order);
    }

    @Override
    public Page<OrderDto> fetchOrdersByUsername(String username, Pageable pageable) {
        Page<Order> ordersPage = orderRepository.findByUserUsernameOrderByDateCreatedDesc(username, pageable);

        List<OrderDto> orderDtoList = ordersPage.getContent().stream()
                .map(OrderMapper::mapToOrderDto)
                .toList();

        return new PageImpl<>(orderDtoList, pageable, ordersPage.getTotalElements());
    }

    @Override
    public OrderDetailsResponse fetchOrderDetails(String email, Long orderId) {

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User", "username", email)
        );

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order", "id", orderId)
        );

        if ((long) order.getUser().getId() != user.getId()) {
            throw new UnauthorizedAccessException("order", "id", orderId);
        }

        return OrderDetailsResponse.builder()
                .orderTrackingNumber(order.getOrderTrackingNumber())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .dateCreated(order.getDateCreated())
                .shippingAddress(getShippingAddressString(order.getShippingAddress()))
                .orderItems(getOrderItemResponses(order.getOrderItems()))
                .build();
    }

    @Override
    public PaymentIntent createPaymentIntent(PaymentInfoRequest paymentInfo) throws StripeException {

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

    private Set<OrderItemResponse> getOrderItemResponses(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::mapToOrderItemResponse)
                .collect(Collectors.toSet());
    }

    private static String getShippingAddressString(Address shippingAddress) {
        return shippingAddress.getStreet() + ", " +
                shippingAddress.getCity() + ", " +
                shippingAddress.getState() + ", " +
                shippingAddress.getCountry() + ", " +
                shippingAddress.getZipCode();
    }

    private OrderItemResponse mapToOrderItemResponse(OrderItem orderItem) {
        Product product = productRepository.findById(orderItem.getProductId()).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", orderItem.getProductId())
        );
        ProductDto productDto = mapToProductDto(product);

        return OrderItemResponse.builder()
                .id(orderItem.getId())
                .unitPrice(BigDecimal.valueOf(1.1))
                .quantity(orderItem.getQuantity())
                .product(productDto)
                .orderId(orderItem.getOrder().getId())
                .build();
    }

}

