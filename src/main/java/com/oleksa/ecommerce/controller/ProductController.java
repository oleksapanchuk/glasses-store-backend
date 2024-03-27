package com.oleksa.ecommerce.controller;

import com.oleksa.ecommerce.constants.AppConstants;
import com.oleksa.ecommerce.dto.ProductDto;
import com.oleksa.ecommerce.dto.ResponseDto;
import com.oleksa.ecommerce.entity.Product;
import com.oleksa.ecommerce.service.ProductService;
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

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createProduct(
            @Valid @RequestBody ProductDto productDto
    ) {
        productService.createProduct(productDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AppConstants.STATUS_201, "Product" + AppConstants.MESSAGE_201));
    }

    @GetMapping("/fetch/{product-id}")
    public ResponseEntity<ProductDto> fetchProduct(
            @PathVariable(name = "product-id") Long productId
    ) {

        ProductDto productDto = productService.fetchProduct(productId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productDto);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateProduct(
            @Valid @RequestBody ProductDto productDto
    ) {
        boolean isUpdated = productService.updateProduct(productDto);

        if (isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AppConstants.STATUS_200, AppConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AppConstants.STATUS_417, AppConstants.MESSAGE_417_UPDATE));
        }
    }

    @PatchMapping("/deactivate/{product-id}")
    public ResponseEntity<ResponseDto> deactivateProduct(
            @PathVariable(name = "product-id") Long productId
    ) {
        boolean isDeleted = productService.deactivateProduct(productId);

        if (isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AppConstants.STATUS_200, AppConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AppConstants.STATUS_417, AppConstants.MESSAGE_417_DELETE));
        }
    }


    @GetMapping("/paginable-list")
    public ResponseEntity<Page<Product>> fetchProductPage(
            Pageable pageable
    ) {

        Page<Product> productsList = productService.getPaginableList(pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productsList);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Product>> searchProducts(
            @RequestParam(name = "search-text") String searchText,
            Pageable pageable
    ) {

        Page<Product> productsList = productService.getPaginableListByNameContaining(searchText, pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productsList);
    }

    @GetMapping("/paginable-list/filters")
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
