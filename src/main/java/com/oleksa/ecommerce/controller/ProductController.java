package com.oleksa.ecommerce.controller;

import com.oleksa.ecommerce.constants.AppConstants;
import com.oleksa.ecommerce.dto.ProductDto;
import com.oleksa.ecommerce.dto.ResponseDto;
import com.oleksa.ecommerce.dto.request.ProductDetailsRequest;
import com.oleksa.ecommerce.dto.response.ProductDetailsResponse;
import com.oleksa.ecommerce.entity.Product;
import com.oleksa.ecommerce.service.ProductService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@Validated
@AllArgsConstructor
@RequestMapping("/api/products")
@RestController
public class ProductController {

    private final ProductService productService;

    @GetMapping("/fetch/{product-id}")
    public ResponseEntity<ProductDetailsResponse> fetchProduct(
            @PathVariable(name = "product-id") Long productId
    ) {

        ProductDetailsResponse product = productService.fetchProduct(productId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(product);
    }

    @GetMapping("/search-products")
    public ResponseEntity<Page<Product>> searchProducts(
            @RequestParam(name = "search-text") String searchText,
            Pageable pageable
    ) {

        Page<Product> productsList = productService.getPaginableListByNameContaining(searchText, pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productsList);
    }

    @GetMapping("/fetch-products")
    public ResponseEntity<Page<Product>> fetchProductPage(
            Pageable pageable
    ) {
        Page<Product> productsList = productService.getPaginableList(pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productsList);
    }

    @GetMapping("/fetch-products-with-filters")
    public ResponseEntity<Page<Product>> fetchProductsWithFilters(
            @RequestParam double minPrice,
            @RequestParam double maxPrice,
            @RequestParam List<Long> categoryIds,
            @RequestParam(name = "sorting-order") String sortingOrder,
            @RequestParam(name = "sorting-method") String sortingMethod,
            Pageable pageable) {

        Page<Product> productsList = productService.getProductsByCategoriesAndPriceRange(minPrice, maxPrice, categoryIds, sortingOrder, sortingMethod, pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productsList);
    }
}
