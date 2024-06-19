package com.oleksa.ecommerce.service;

import com.oleksa.ecommerce.dto.AddressDto;
import com.oleksa.ecommerce.dto.OrderDto;
import com.oleksa.ecommerce.dto.request.PurchaseRequest;
import com.oleksa.ecommerce.dto.response.OrderDetailsResponse;
import com.oleksa.ecommerce.entity.*;
import com.oleksa.ecommerce.exception.ResourceNotFoundException;
import com.oleksa.ecommerce.repository.OrderRepository;
import com.oleksa.ecommerce.repository.ProductRepository;
import com.oleksa.ecommerce.repository.UserRepository;
import com.oleksa.ecommerce.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @InjectMocks
    private OrderServiceImpl orderService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderRepository orderRepository;


    private PurchaseRequest purchaseRequest;

    @BeforeEach
    void setUp() {
        purchaseRequest = PurchaseRequest.builder()
                .userId(1L)
                .address(
                        AddressDto.builder()
                                .city("city")
                                .country("country")
                                .state("state")
                                .zipCode("zipCode")
                                .build())
                .orderItems(
                        Set.of(OrderItem.builder()
                                .productId(1L)
                                .quantity(2)
                                .build())
                )
                .build();
    }

    @Test
    void shouldCreateOrderSuccessfully_WhenValidRequestIsGiven() {
        // Given
        String email = "test@example.com";
        User user = new User();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(orderRepository.save(any(Order.class))).thenReturn(new Order());
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(Product.builder()
                .id(1L)
                .price(100.0)
                .unitsInStock(10)
                .build()));

        // When
        OrderDto result = orderService.createOrder(email, purchaseRequest);

        // Then
        assertNotNull(result);
        verify(userRepository, times(1)).findByEmail(email);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenUserNotFoundOnCreateOrder() {
        String email = "nonExistingEmail@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(email, purchaseRequest));

        verify(userRepository, times(1)).findByEmail(email);
        verify(orderRepository, times(0)).save(any(Order.class));
    }

    @Test
    void shouldThrowIllegalArgumentException_WhenPurchaseRequestIsNullOnCreateOrder() {
        String email = "test@example.com";
        User user = new User();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(email, null));

        verify(userRepository, times(1)).findByEmail(email);
        verify(orderRepository, times(0)).save(any(Order.class));
    }

    @Test
    void shouldFetchOrderByIdSuccessfully_WhenValidUsernameAndOrderIdAreGiven() {
        // Given
        String email = "test@example.com";
        Long orderId = 1L;
        User user = new User();
        user.setId(1L);  // Setting a valid user ID
        Order order = new Order();
        order.setUser(user);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // When
        OrderDto result = orderService.fetchOrderById(email, orderId);

        // Then
        assertNotNull(result);
        verify(userRepository, times(1)).findByEmail(email);
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenOrderNotFoundOnFetchOrderById() {
        String email = "test@example.com";
        Long orderId = 1L;
        User user = new User();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.fetchOrderById(email, orderId));

        verify(userRepository, times(1)).findByEmail(email);
        verify(orderRepository, times(1)).findById(orderId);
    }


    @Test
    void shouldThrowResourceNotFoundException_WhenOrderNotFoundOnFetchOrderByTrackingNumber() {
        String email = "test@example.com";
        String orderTrackingNumber = "nonExistingTrackingNumber";
        User user = new User();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(orderRepository.findByOrderTrackingNumber(orderTrackingNumber)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.fetchOrderByTrackingNumber(email, orderTrackingNumber));

        verify(userRepository, times(1)).findByEmail(email);
        verify(orderRepository, times(1)).findByOrderTrackingNumber(orderTrackingNumber);
    }

    @Test
    void shouldFetchOrdersByEmailSuccessfully_WhenValidUsernameIsGiven() {
        String email = "test@example.com";
        Pageable pageable = PageRequest.of(0, 5);
        User user = new User();
        Order order = new Order();
        order.setUser(user);
        Page<Order> ordersPage = new PageImpl<>(List.of(order), pageable, 1);
        when(orderRepository.findByUserEmailOrderByDateCreatedDesc(email, pageable)).thenReturn(ordersPage);

        Page<OrderDto> result = orderService.fetchOrdersByEmail(email, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(orderRepository, times(1)).findByUserEmailOrderByDateCreatedDesc(email, pageable);
    }

    @Test
    void shouldReturnEmptyPage_WhenNoOrdersFoundForUserOnFetchOrdersByEmail() {
        String email = "test@example.com";
        Pageable pageable = PageRequest.of(0, 5);
        Page<Order> ordersPage = Page.empty(pageable);
        when(orderRepository.findByUserEmailOrderByDateCreatedDesc(email, pageable)).thenReturn(ordersPage);

        Page<OrderDto> result = orderService.fetchOrdersByEmail(email, pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(orderRepository, times(1)).findByUserEmailOrderByDateCreatedDesc(email, pageable);
    }

    @Test
    void shouldFetchOrderDetailsSuccessfully_WhenValidUsernameAndOrderIdAreGiven() {
        // Given
        String email = "test@example.com";
        Long orderId = 1L;
        User user = new User();
        user.setId(1L);  // Setting a valid user ID
        Order order = new Order();
        order.setUser(user);

        // Set up a valid shipping address
        Address shippingAddress = new Address();
        shippingAddress.setStreet("123 Main St");
        shippingAddress.setCity("Test City");
        shippingAddress.setZipCode("12345");
        order.setShippingAddress(shippingAddress);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // When
        OrderDetailsResponse result = orderService.fetchOrderDetails(email, orderId);

        // Then
        assertNotNull(result);
        verify(userRepository, times(1)).findByEmail(email);
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenOrderNotFoundOnFetchOrderDetails() {
        String email = "test@example.com";
        Long orderId = 1L;
        User user = new User();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.fetchOrderDetails(email, orderId));

        verify(userRepository, times(1)).findByEmail(email);
        verify(orderRepository, times(1)).findById(orderId);
    }

}
