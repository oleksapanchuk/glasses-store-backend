package com.oleksa.ecommerce.controller;

import com.oleksa.ecommerce.dto.ProductDto;
import com.oleksa.ecommerce.entity.Product;
import com.oleksa.ecommerce.mapper.ProductMapper;
import com.oleksa.ecommerce.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/products", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Validated
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{product-id}")
    public ResponseEntity<Product> getProduct(
            @PathVariable(name = "product-id") Long productId
    ) {

        Product product = productService.getProduct(productId)
                .orElseThrow(() -> new RuntimeException("error on product fetching"));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(product);
    }

    @GetMapping("/paginable-list")
    public ResponseEntity<Page<Product>> fetchProductPage(
            Pageable pageable
    ) {

        Page<Product> productsList = productService.getPaginableList(pageable)
                .orElseThrow(() -> new RuntimeException("error on product fetching"));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productsList);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Product>> fetchProductPage(
            @RequestParam(name = "search-text") String searchText,
            Pageable pageable
    ) {

        Page<Product> productsList = productService.getPaginableListByNameContaining(searchText, pageable)
                .orElseThrow(() -> new RuntimeException("error on product fetching"));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productsList);
    }

    @GetMapping("/paginable-list/filters")
    public ResponseEntity<Page<Product>> fetchProductPage(
            @RequestParam double minPrice,
            @RequestParam double maxPrice,
            @RequestParam List<Long> categoryIds,
            @RequestParam(name = "sorting-order") String sortingOrder,
            @RequestParam(name = "sorting-method") String sortingMethod,
            Pageable pageable) {

        Page<Product> productsList = productService.getProductsByCategoriesAndPriceRange(minPrice, maxPrice, categoryIds, sortingOrder, sortingMethod, pageable)
                .orElseThrow(() -> new RuntimeException("error on product fetching"));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productsList);
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {

        Product product = productService.saveProduct(productDto)
                .orElseThrow();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ProductMapper.mapToProductDto(product));
    }

}
