package com.oleksa.ecommerce.service.impl;

import com.oleksa.ecommerce.dto.ProductDto;
import com.oleksa.ecommerce.entity.Product;
import com.oleksa.ecommerce.entity.add.SortingOrder;
import com.oleksa.ecommerce.repository.ProductRepository;
import com.oleksa.ecommerce.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Optional<Product> getProduct(Long productId) {
        return productRepository.findById(productId);
    }

    @Override
    public Optional<Page<Product>> getPaginableList(Pageable pageable) {

        return Optional.of(productRepository.findAll(pageable));

    }

    @Override
    public Optional<Page<Product>> getPaginableListByNameContaining(String text, Pageable pageable) {

        System.out.println("search text: " + text);

        return Optional.of(productRepository.findByNameContaining(text, pageable));

    }

    @Override
    public Optional<Page<Product>> getProductsByCategoriesAndPriceRange(double minPrice, double maxPrice, List<Long> categoryIds, String sortingOrder, String sortingField, Pageable pageable) {

        var order = Sort.Direction.ASC;
        if (sortingOrder.trim().equals(SortingOrder.DESC.label)) {
            order = Sort.Direction.DESC;
        }

        List<Long> idList = new ArrayList<>();
        for (Long i : categoryIds) {
            if (i != 0) {
                idList.add(i);
            }
        }

        if (idList.isEmpty()) {
            return Optional.of(productRepository.findProductsByPriceRange(
                    minPrice,
                    maxPrice,
                    PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(order, sortingField))));
        }

        return Optional.of(productRepository.findProductsByCategoriesAndPriceRange(
                minPrice,
                maxPrice,
                idList,
                idList.size(),
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(order, "p." + sortingField))
        ));
    }

    @Override
    public Optional<Product> saveProduct(ProductDto productDto) {
        return Optional.empty();
    }

    @Override
    public Optional<Product> updateProduct(ProductDto productDto) {
        return Optional.empty();
    }

    @Override
    public boolean deleteProduct(ProductDto productDto) {
        return false;
    }
}