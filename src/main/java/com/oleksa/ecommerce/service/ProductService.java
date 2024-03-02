package com.oleksa.ecommerce.service;

import com.oleksa.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Optional<Product> getProduct(Long productId);

    Optional<Page<Product>> getPaginableList(Pageable pageable);

    Optional<Page<Product>> getPaginableListByNameContaining(String text, Pageable pageable);

    Optional<Page<Product>> getProductsByCategoriesAndPriceRange(double minPrice, double maxPrice, List<Long> categoryIds,  String sortingOrder, String sortingField, Pageable pageable);

}
