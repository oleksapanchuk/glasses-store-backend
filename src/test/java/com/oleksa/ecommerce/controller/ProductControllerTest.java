package com.oleksa.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oleksa.ecommerce.dto.ProductDto;
import com.oleksa.ecommerce.service.JwtService;
import com.oleksa.ecommerce.service.ProductService;
import com.oleksa.ecommerce.service.UserService;
import org.junit.jupiter.api.BeforeEach;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ProductController.class})
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private UserService userService;

    ProductDto productDto;

    @BeforeEach
    void setUp() {
        productDto = ProductDto.builder()
                .id(1L)
                .sku("123456")
                .name("Test Product")
                .description("Test Description")
                .price(100.0)
                .unitsInStock(10)
                .imageUrl("test-image.jpg")
                .active(true)
                .build();
    }

    @Test
    void shouldCreateProductSuccessfully_WhenValidProductDtoIsGiven() throws Exception {
        doNothing().when(productService).createProduct(productDto);

        mockMvc.perform(post("/api/products/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productDto)))
                .andExpect(status().isCreated());

        verify(productService, times(1)).createProduct(productDto);
    }

    @Test
    void shouldFetchProductSuccessfully_WhenValidProductIdIsGiven() throws Exception {
        Long productId = 1L;
        productDto.setId(productId);
        when(productService.fetchProduct(productId)).thenReturn(productDto);

        mockMvc.perform(get("/api/products/fetch/" + productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(productDto.getName())))
                .andExpect(jsonPath("$.description", is(productDto.getDescription())))
                .andExpect(jsonPath("$.price", is(productDto.getPrice())));

        verify(productService, times(1)).fetchProduct(productId);
    }

    @Test
    void shouldUpdateProductSuccessfully_WhenValidProductDtoIsGiven() throws Exception {
        productDto.setId(1L);
        when(productService.updateProduct(productDto)).thenReturn(true);

        mockMvc.perform(put("/api/products/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productDto)))
                .andExpect(status().isOk());

        verify(productService, times(1)).updateProduct(productDto);
    }

    @Test
    void shouldDeactivateProductSuccessfully_WhenValidProductIdIsGiven() throws Exception {
        Long productId = 1L;
        when(productService.deactivateProduct(productId)).thenReturn(true);

        mockMvc.perform(patch("/api/products/deactivate/" + productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(productService, times(1)).deactivateProduct(productId);
    }

    @Test
    void shouldFetchProductPageSuccessfully() throws Exception {
        when(productService.getPaginableList(any())).thenReturn(null);

        mockMvc.perform(get("/api/products/paginable-list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(productService, times(1)).getPaginableList(any());
    }

    @Test
    void shouldSearchProductsSuccessfully_WhenValidSearchTextIsGiven() throws Exception {
        String searchText = "Test";
        when(productService.getPaginableListByNameContaining(anyString(), any())).thenReturn(null);

        mockMvc.perform(get("/api/products/search")
                        .param("search-text", searchText)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(productService, times(1)).getPaginableListByNameContaining(anyString(), any());
    }

    @Test
    void shouldFetchProductsWithFiltersSuccessfully_WhenValidFiltersAreGiven() throws Exception {
        double minPrice = 50.0;
        double maxPrice = 150.0;
        List<Long> categoryIds = Arrays.asList(1L, 2L);
        String sortingOrder = "asc";
        String sortingMethod = "price";
        when(productService.getProductsByCategoriesAndPriceRange(anyDouble(), anyDouble(), anyList(), anyString(), anyString(), any())).thenReturn(null);

        mockMvc.perform(get("/api/products/paginable-list/filters")
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
