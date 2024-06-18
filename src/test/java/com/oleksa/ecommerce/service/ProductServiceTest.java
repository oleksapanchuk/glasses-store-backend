package com.oleksa.ecommerce.service;

import com.oleksa.ecommerce.dto.ProductDto;
import com.oleksa.ecommerce.dto.request.ProductDetailsRequest;
import com.oleksa.ecommerce.dto.response.ProductDetailsResponse;
import com.oleksa.ecommerce.entity.Product;
import com.oleksa.ecommerce.exception.ResourceNotFoundException;
import com.oleksa.ecommerce.repository.ProductRepository;
import com.oleksa.ecommerce.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductServiceImpl productService;
    @Mock
    private ProductRepository productRepository;

    private Product product;
    private ProductDto productDto;
    private ProductDetailsRequest productDetailsRequest;

    @BeforeEach
    public void setup() {
        product = new Product();
        product.setId(1L);

        productDto = ProductDto.builder()
                .id(1L)
                .sku("testSku")
                .name("testName")
                .description("testDescription")
                .price(100.0)
                .unitsInStock(10)
                .imageUrl("testImageUrl")
                .available(true)
                .build();

        productDetailsRequest = ProductDetailsRequest.builder()
                .id(1L)
                .sku("testSku")
                .name("testName")
                .description("testDescription")
                .price(100.0)
                .unitsInStock(10)
                .imageUrl("testImageUrl")
                .active(true)
                .build();

        Product product = new Product();
        product.setId(1L);
//        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
//        when(productRepository.save(any(Product.class))).thenReturn(product);
    }

    @Test
    void shouldCreateProductSuccessfully() {
        when(productRepository.save(any(Product.class))).thenReturn(product);
        productService.createProduct(productDetailsRequest);

        assertNotNull(productDto);
    }

    @Test
    void shouldFetchProductSuccessfully() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        ProductDetailsResponse result = productService.fetchProduct(1L);

        assertNotNull(result);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFoundOnFetchProduct() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.fetchProduct(1L)
        );
    }

    @Test
    void shouldUpdateProductSuccessfully() {
        productDto.setId(1L);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        boolean result = productService.updateProduct(productDetailsRequest);

        assertTrue(result);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFoundOnUpdateProduct() {
        productDto.setId(1L);
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(productDetailsRequest));
    }

    @Test
    void shouldDeactivateProductSuccessfully() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        boolean result = productService.deactivateProduct(1L);
        assertTrue(result);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFoundOnDeactivateProduct() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.deactivateProduct(1L));
    }

    @Test
    void shouldGetPaginableListSuccessfully() {
        when(productRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        Page<Product> result = productService.getPaginableList(PageRequest.of(0, 10));

        assertNotNull(result);
    }

    @Test
    void shouldGetPaginableListByNameContainingSuccessfully() {
        when(productRepository.findByNameContaining(anyString(), any(Pageable.class))).thenReturn(Page.empty());

        Page<Product> result = productService.getPaginableListByNameContaining(
                "test",
                PageRequest.of(0, 10)
        );

        assertNotNull(result);
    }

    @Test
    void shouldGetProductsByPriceRangeSuccessfullyWhenNoCategoriesProvided() {
        List<Long> categoryIds = Arrays.asList(0L);
        Pageable pageable = PageRequest.of(0, 10);
        when(productRepository.findProductsByPriceRange(anyDouble(), anyDouble(), any(Pageable.class))).thenReturn(Page.empty());

        Page<Product> result = productService.getProductsByCategoriesAndPriceRange(100.0, 200.0, categoryIds, "ASC", "price", pageable);

        assertNotNull(result);
    }


}
