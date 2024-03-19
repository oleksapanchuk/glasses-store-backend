package com.oleksa.ecommerce.mapper;

import com.oleksa.ecommerce.dto.ProductDto;
import com.oleksa.ecommerce.entity.Product;

public class ProductMapper {

    public static ProductDto mapToProductDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .sku(product.getSku())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .active(product.isActive())
                .unitsInStock(product.getUnitsInStock())
                .imageUrl(product.getImageUrl())
                .build();
    }

    public static Product mapToProduct(Product product, ProductDto productDto) {
        product.setSku(productDto.getSku());
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setActive(productDto.getActive());
        product.setUnitsInStock(productDto.getUnitsInStock());
        product.setImageUrl(productDto.getImageUrl());

        return product;
    }
}