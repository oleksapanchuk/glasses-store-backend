package com.oleksa.ecommerce.service;

import com.oleksa.ecommerce.dto.AddressDto;
import com.oleksa.ecommerce.dto.OrderDto;
import com.oleksa.ecommerce.dto.request.PurchaseRequest;
import com.oleksa.ecommerce.dto.response.OrderDetailsResponse;
import com.oleksa.ecommerce.entity.Address;
import com.oleksa.ecommerce.entity.Order;
import com.oleksa.ecommerce.entity.OrderItem;
import com.oleksa.ecommerce.entity.User;
import com.oleksa.ecommerce.exception.ResourceNotFoundException;
import com.oleksa.ecommerce.exception.UnauthorizedAccessException;
import com.oleksa.ecommerce.repository.OrderRepository;
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

import java.math.BigDecimal;
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
    private OrderRepository orderRepository;


    private PurchaseRequest purchaseRequest;

    @BeforeEach
    void setUp() {
        purchaseRequest = PurchaseRequest.builder()
                .shippingAddress(
                        AddressDto.builder()
                                .city("city")
                                .country("country")
                                .state("state")
                                .zipCode("zipCode")
                                .build())
                .order(
                        OrderDto.builder()
                                .orderTrackingNumber("orderTrackingNumber")
                                .totalPrice(100)
                                .totalQuantity(3)
                                .status("ACCEPTED")
                                .build()
                )
                .orderItems(
                        Set.of(OrderItem.builder()
                                .unitPrice(BigDecimal.ONE)
                                .productId(1L)
                                .quantity(2)
                                .build())
                )
                .build();
    }

    @Test
    void shouldCreateOrderSuccessfully_WhenValidRequestIsGiven() {
        // Arrange
        String username = "testUser";
        User user = new User();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(orderRepository.save(any(Order.class))).thenReturn(new Order());

        // Act
        OrderDto result = orderService.createOrder(username, purchaseRequest);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).findByUsername(username);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenUserNotFound() {
        // Arrange
        String username = "testUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(username, purchaseRequest));

        verify(userRepository, times(1)).findByUsername(username);
        verify(orderRepository, times(0)).save(any(Order.class));
    }

    @Test
    void shouldThrowIllegalArgumentException_WhenPurchaseRequestIsNull() {
        // Arrange
        String username = "testUser";
        User user = new User();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(username, null));

        verify(userRepository, times(1)).findByUsername(username);
        verify(orderRepository, times(0)).save(any(Order.class));
    }

    @Test
    void shouldFetchOrderByIdSuccessfully_WhenValidUsernameAndOrderIdAreGiven() {
        String username = "testUser";
        Long orderId = 1L;
        User user = new User();
        user.setId(1L);
        Order order = mock(Order.class);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(order.getUser()).thenReturn(user);

        OrderDto result = orderService.fetchOrderById(username, orderId);

        assertNotNull(result);
        verify(userRepository, times(1)).findByUsername(username);
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenOrderNotFound() {
        String username = "testUser";
        Long orderId = 1L;
        User user = new User();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.fetchOrderById(username, orderId));

        verify(userRepository, times(1)).findByUsername(username);
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void shouldThrowUnauthorizedAccessException_WhenOrderDoesNotBelongToUser() {
        String username = "testUser";
        Long orderId = 1L;
        User user = new User();
        user.setId(1L);
        User anotherUser = new User();
        anotherUser.setId(2L);
        Order order = mock(Order.class);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(order.getUser()).thenReturn(anotherUser);

        assertThrows(UnauthorizedAccessException.class, () -> orderService.fetchOrderById(username, orderId));

        verify(userRepository, times(1)).findByUsername(username);
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void testFetchOrderByTrackingNumber_shouldFetchOrderByTrackingNumberSuccessfully_WhenValidUsernameAndTrackingNumberAreGiven() {
        String username = "testUser";
        String orderTrackingNumber = "trackingNumber";
        User user = new User();
        user.setId(1L);
        Order order = mock(Order.class);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(orderRepository.findByOrderTrackingNumber(orderTrackingNumber)).thenReturn(Optional.of(order));
        when(order.getUser()).thenReturn(user);

        OrderDto result = orderService.fetchOrderByTrackingNumber(username, orderTrackingNumber);

        assertNotNull(result);
        verify(userRepository, times(1)).findByUsername(username);
        verify(orderRepository, times(1)).findByOrderTrackingNumber(orderTrackingNumber);
    }

    @Test
    void testFetchOrderByTrackingNumber_shouldThrowResourceNotFoundException_WhenUserNotFound() {
        String username = "testUser";
        String orderTrackingNumber = "trackingNumber";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.fetchOrderByTrackingNumber(username, orderTrackingNumber));

        verify(userRepository, times(1)).findByUsername(username);
        verify(orderRepository, times(0)).findByOrderTrackingNumber(orderTrackingNumber);
    }

    @Test
    void testFetchOrderByTrackingNumber_shouldThrowResourceNotFoundException_WhenOrderNotFound() {
        String username = "testUser";
        String orderTrackingNumber = "trackingNumber";
        User user = new User();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(orderRepository.findByOrderTrackingNumber(orderTrackingNumber)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.fetchOrderByTrackingNumber(username, orderTrackingNumber));

        verify(userRepository, times(1)).findByUsername(username);
        verify(orderRepository, times(1)).findByOrderTrackingNumber(orderTrackingNumber);
    }

    @Test
    void testFetchOrderByTrackingNumber_shouldThrowUnauthorizedAccessException_WhenOrderDoesNotBelongToUser() {
        String username = "testUser";
        String orderTrackingNumber = "trackingNumber";
        User user = new User();
        user.setId(1L);
        User anotherUser = new User();
        anotherUser.setId(2L);
        Order order = mock(Order.class);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(orderRepository.findByOrderTrackingNumber(orderTrackingNumber)).thenReturn(Optional.of(order));
        when(order.getUser()).thenReturn(anotherUser);

        assertThrows(UnauthorizedAccessException.class, () -> orderService.fetchOrderByTrackingNumber(username, orderTrackingNumber));

        verify(userRepository, times(1)).findByUsername(username);
        verify(orderRepository, times(1)).findByOrderTrackingNumber(orderTrackingNumber);
    }

    @Test
    void testFetchOrdersByUsername_shouldFetchOrdersByUsernameSuccessfully_WhenValidUsernameIsGiven() {
        String username = "testUser";
        Pageable pageable = PageRequest.of(0, 5);
        User user = new User();
        user.setUsername(username);
        Order order = new Order();
        order.setUser(user);
        Page<Order> ordersPage = new PageImpl<>(List.of(order), pageable, 1);
        when(orderRepository.findByUserUsernameOrderByDateCreatedDesc(username, pageable)).thenReturn(ordersPage);

        Page<OrderDto> result = orderService.fetchOrdersByUsername(username, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(orderRepository, times(1)).findByUserUsernameOrderByDateCreatedDesc(username, pageable);
    }

    @Test
    void testFetchOrdersByUsername_shouldReturnEmptyPage_WhenNoOrdersFoundForUser() {
        String username = "testUser";
        Pageable pageable = PageRequest.of(0, 5);
        User user = new User();
        user.setUsername(username);
        Page<Order> ordersPage = Page.empty(pageable);
        when(orderRepository.findByUserUsernameOrderByDateCreatedDesc(username, pageable)).thenReturn(ordersPage);

        Page<OrderDto> result = orderService.fetchOrdersByUsername(username, pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(orderRepository, times(1)).findByUserUsernameOrderByDateCreatedDesc(username, pageable);
    }

    @Test
    void testFetchOrderDetails_shouldFetchOrderDetailsSuccessfully_WhenValidUsernameAndOrderIdAreGiven() {
        String username = "testUser";
        Long orderId = 1L;
        User user = new User();
        user.setId(1L);
        Order order = mock(Order.class);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(order.getUser()).thenReturn(user);
        when(order.getShippingAddress()).thenReturn(new Address());

        OrderDetailsResponse result = orderService.fetchOrderDetails(username, orderId);

        assertNotNull(result);
        verify(userRepository, times(1)).findByUsername(username);
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void testFetchOrderDetails_shouldThrowResourceNotFoundException_WhenUserNotFound() {
        String username = "testUser";
        Long orderId = 1L;
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.fetchOrderDetails(username, orderId));

        verify(userRepository, times(1)).findByUsername(username);
        verify(orderRepository, times(0)).findById(orderId);
    }

    @Test
    void testFetchOrderDetails_shouldThrowResourceNotFoundException_WhenOrderNotFound() {
        String username = "testUser";
        Long orderId = 1L;
        User user = new User();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.fetchOrderDetails(username, orderId));

        verify(userRepository, times(1)).findByUsername(username);
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void testFetchOrderDetails_shouldThrowUnauthorizedAccessException_WhenOrderDoesNotBelongToUser() {
        String username = "testUser";
        Long orderId = 1L;
        User user = new User();
        user.setId(1L);
        User anotherUser = new User();
        anotherUser.setId(2L);
        Order order = mock(Order.class);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(order.getUser()).thenReturn(anotherUser);

        assertThrows(UnauthorizedAccessException.class, () -> orderService.fetchOrderDetails(username, orderId));

        verify(userRepository, times(1)).findByUsername(username);
        verify(orderRepository, times(1)).findById(orderId);
    }

}
