package com.oleksa.ecommerce.controller;

import com.oleksa.ecommerce.dto.response.ProductDetailsResponse;
import com.oleksa.ecommerce.service.AuthenticationService;
import com.oleksa.ecommerce.service.JwtService;
import com.oleksa.ecommerce.service.ProductService;
import com.oleksa.ecommerce.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {

    private static final String BASE_PRODUCTS_URL = "/api/products";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService productService;
    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private UserService userService;

    @Test
    void shouldFetchProductSuccessfully_WhenValidProductIdIsGiven() throws Exception {
        Long productId = 1L;
        ProductDetailsResponse productDetailsResponse = ProductDetailsResponse.builder().build();
        when(productService.fetchProduct(productId)).thenReturn(productDetailsResponse);

        mockMvc.perform(get(BASE_PRODUCTS_URL + "/fetch/" + productId))
                .andExpect(status().isOk());

        verify(productService, times(1)).fetchProduct(productId);
    }

    @Test
    void shouldSearchProductsSuccessfully_WhenValidSearchTextIsGiven() throws Exception {
        String searchText = "Test";
        when(productService.getPaginableListByNameContaining(anyString(), any())).thenReturn(null);

        mockMvc.perform(get(BASE_PRODUCTS_URL + "/search-products")
                        .param("search-text", searchText)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(productService, times(1)).getPaginableListByNameContaining(anyString(), any());
    }


    @Test
    void shouldFetchProductPageSuccessfully() throws Exception {
        when(productService.getPaginableList(any())).thenReturn(null);

        mockMvc.perform(get(BASE_PRODUCTS_URL + "/fetch-products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(productService, times(1)).getPaginableList(any());
    }

    @Test
    void shouldFetchProductsWithFiltersSuccessfully_WhenValidFiltersAreGiven() throws Exception {
        double minPrice = 50.0;
        double maxPrice = 150.0;
        List<Long> categoryIds = Arrays.asList(1L, 2L);
        String sortingOrder = "asc";
        String sortingMethod = "price";
        when(productService.getProductsByCategoriesAndPriceRange(anyDouble(), anyDouble(), anyList(), anyString(), anyString(), any())).thenReturn(null);

        mockMvc.perform(get(BASE_PRODUCTS_URL + "/fetch-products-with-filters")
                        .param("minPrice", String.valueOf(minPrice))
                        .param("maxPrice", String.valueOf(maxPrice))
                        .param("categoryIds", categoryIds.stream().map(Object::toString).collect(Collectors.joining(",")))
                        .param("sorting-order", sortingOrder)
                        .param("sorting-method", sortingMethod)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(productService, times(1)).getProductsByCategoriesAndPriceRange(anyDouble(), anyDouble(), anyList(), anyString(), anyString(), any());
    }

}
