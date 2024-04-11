package com.oleksa.ecommerce.service;

import com.oleksa.ecommerce.dto.request.ProductDetailsRequest;
import com.oleksa.ecommerce.dto.response.ProductDetailsResponse;
import com.oleksa.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    Long createProduct(ProductDetailsRequest productDto);

    ProductDetailsResponse fetchProduct(Long productId);

    boolean updateProduct(ProductDetailsRequest productDto);

    boolean deactivateProduct(Long productId);

    Page<Product> getPaginableList(Pageable pageable);

    Page<Product> getPaginableListByNameContaining(String text, Pageable pageable);

    Page<Product> getProductsByCategoriesAndPriceRange(double minPrice, double maxPrice, List<Long> categoryIds, String sortingOrder, String sortingField, Pageable pageable);

}
