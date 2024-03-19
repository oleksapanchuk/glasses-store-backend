package com.oleksa.ecommerce.service.impl;

import com.oleksa.ecommerce.dto.ProductDto;
import com.oleksa.ecommerce.entity.Product;
import com.oleksa.ecommerce.entity.add.SortingOrder;
import com.oleksa.ecommerce.exception.ResourceNotFoundException;
import com.oleksa.ecommerce.mapper.ProductMapper;
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

@AllArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    @Override
    public void createProduct(ProductDto productDto) {

        Product product = ProductMapper.mapToProduct(new Product(), productDto);

        repository.save(product);
    }

    @Override
    public ProductDto fetchProduct(Long productId) {

        Product product = repository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "productId", productId)
        );

        return ProductMapper.mapToProductDto(product);
    }

    @Override
    public boolean updateProduct(ProductDto productDto) {

        Product product = repository.findById(productDto.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Product", "productId", productDto.getId())
        );

        repository.save(ProductMapper.mapToProduct(product, productDto));

        return true;
    }

    @Override
    public boolean deactivateProduct(Long productId) {

        Product product = repository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "productId", productId)
        );

        product.setActive(false);

        repository.save(product);

        return true;
    }

    @Override
    public Page<Product> getPaginableList(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Product> getPaginableListByNameContaining(String text, Pageable pageable) {
        return repository.findByNameContaining(text, pageable);
    }

    @Override
    public Page<Product> getProductsByCategoriesAndPriceRange(double minPrice, double maxPrice, List<Long> categoryIds, String sortingOrder, String sortingField, Pageable pageable) {

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
            return repository.findProductsByPriceRange(
                    minPrice,
                    maxPrice,
                    PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(order, sortingField)));
        }

        return repository.findProductsByCategoriesAndPriceRange(
                minPrice,
                maxPrice,
                idList,
                idList.size(),
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(order, "p." + sortingField))
        );
    }

}