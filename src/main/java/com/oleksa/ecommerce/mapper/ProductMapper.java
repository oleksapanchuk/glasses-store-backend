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
                .rating(product.getRating())
                .active(product.isActive())
                .unitsInStock(product.getUnitsInStock())
                .build();
    }

    public static Product mapToProduct(ProductDto productDto) {
        return Product.builder()
                .id(productDto.getId())
                .sku(productDto.getSku())
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .rating(productDto.getRating())
                .active(productDto.getActive())
                .unitsInStock(productDto.getUnitsInStock())
                .build();
    }
}