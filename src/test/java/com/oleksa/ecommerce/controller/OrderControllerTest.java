package com.oleksa.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oleksa.ecommerce.dto.OrderDto;
import com.oleksa.ecommerce.dto.request.PurchaseRequest;
import com.oleksa.ecommerce.dto.response.OrderDetailsResponse;
import com.oleksa.ecommerce.service.JwtService;
import com.oleksa.ecommerce.service.OrderService;
import com.oleksa.ecommerce.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({OrderController.class})
@AutoConfigureMockMvc(addFilters = false)
public class OrderControllerTest {

    private final String BASE_ORDERS_URL = "/api/orders";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private UserService userService;

    private String username;

    @BeforeEach
    void setUp() {
        username = "testUser";
        Authentication auth = new TestingAuthenticationToken(username, null);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void shouldFetchOrderByIdSuccessfully_WhenValidOrderIdIsGiven() throws Exception {
        Long orderId = 1L;
        OrderDto orderDto = OrderDto.builder()
                .id(orderId)
                .orderTrackingNumber("123456")
                .build();
        when(orderService.fetchOrderById(username, orderId)).thenReturn(orderDto);

        mockMvc.perform(get(BASE_ORDERS_URL + "/fetch-by-id/" + orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(orderDto.getId().intValue())))
                .andExpect(jsonPath("$.orderTrackingNumber", is(orderDto.getOrderTrackingNumber())));

        verify(orderService, times(1)).fetchOrderById(username, orderId);
    }

    @Test
    void shouldFetchOrderByTrackingNumberSuccessfully_WhenValidTrackingNumberIsGiven() throws Exception {
        String trackingNumber = "123456";
        OrderDto orderDto = OrderDto.builder()
                .id(1L)
                .orderTrackingNumber(trackingNumber)
                .build();
        when(orderService.fetchOrderByTrackingNumber(username, trackingNumber)).thenReturn(orderDto);

        mockMvc.perform(get(BASE_ORDERS_URL + "/fetch-by-tracking-number/" + trackingNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(orderDto.getId().intValue())))
                .andExpect(jsonPath("$.orderTrackingNumber", is(orderDto.getOrderTrackingNumber())));

        verify(orderService, times(1)).fetchOrderByTrackingNumber(username, trackingNumber);
    }

    @Test
    void shouldFetchOrdersByUsernameSuccessfully_WhenValidUsernameIsGiven() throws Exception {
        when(orderService.fetchOrdersByEmail(anyString(), any())).thenReturn(null);

        mockMvc.perform(get(BASE_ORDERS_URL + "/fetch-by-email")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(orderService, times(1)).fetchOrdersByEmail(username, PageRequest.of(0, 20));
    }

    @Test
    void shouldFetchOrderDetailsSuccessfully_WhenValidOrderIdIsGiven() throws Exception {
        Long orderId = 1L;
        String trackingNumber = "123456";
        OrderDetailsResponse orderDetailsResponse = OrderDetailsResponse.builder()
                .orderTrackingNumber(trackingNumber)
                .build();
        when(orderService.fetchOrderDetails(username, orderId)).thenReturn(orderDetailsResponse);

        mockMvc.perform(get(BASE_ORDERS_URL + "/fetch-order-details/" + orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderTrackingNumber", is(orderDetailsResponse.getOrderTrackingNumber())));

        verify(orderService, times(1)).fetchOrderDetails(username, orderId);
    }

    @Test
    void shouldPlaceOrderSuccessfully_WhenValidPurchaseRequestIsGiven() throws Exception {
        PurchaseRequest purchaseRequest = PurchaseRequest.builder().build();
        OrderDto orderDto = OrderDto.builder()
                .id(1L)
                .orderTrackingNumber("123456")
                .build();
        when(orderService.createOrder(username, purchaseRequest)).thenReturn(orderDto);

        mockMvc.perform(post(BASE_ORDERS_URL + "/place-order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(purchaseRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(orderDto.getId().intValue())))
                .andExpect(jsonPath("$.orderTrackingNumber", is(orderDto.getOrderTrackingNumber())));

        verify(orderService, times(1)).createOrder(username, purchaseRequest);
    }

}
