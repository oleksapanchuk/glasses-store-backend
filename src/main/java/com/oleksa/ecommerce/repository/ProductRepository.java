package com.oleksa.ecommerce.repository;

import com.oleksa.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByNameContaining(@Param("name") String name, Pageable pageable);

    @Query(value = """
                SELECT *
                FROM products p
                WHERE p.product_price BETWEEN :minPrice AND :maxPrice
            """, nativeQuery = true)
    Page<Product> findProductsByPriceRange(
            @Param("minPrice") double minPrice,
            @Param("maxPrice") double maxPrice,
            Pageable pageable);

    @Query(value = """
                SELECT p.*
                FROM (
                  SELECT p.product_id
                  FROM products p
                  JOIN product_has_category phc ON p.product_id = phc.product_id
                  WHERE p.product_price BETWEEN :minPrice AND :maxPrice
                  AND phc.category_id IN (:categoryIds)
                  GROUP BY p.product_id
                  HAVING COUNT(DISTINCT phc.category_id) = :numCategories
                ) AS filtered_products
                JOIN products p ON filtered_products.product_id = p.product_id
                
            """,
            countQuery = """
                        SELECT COUNT(*)
                        FROM (
                          SELECT 1
                          FROM products p
                          JOIN product_has_category phc ON p.product_id = phc.product_id
                          WHERE p.product_price BETWEEN :minPrice AND :maxPrice
                          AND phc.category_id IN (:categoryIds)
                          GROUP BY p.product_id
                          HAVING COUNT(DISTINCT phc.category_id) = :numCategories
                        ) AS filtered_products
                    """,
            nativeQuery = true)
    Page<Product> findProductsByCategoriesAndPriceRange(
            @Param("minPrice") double minPrice,
            @Param("maxPrice") double maxPrice,
            @Param("categoryIds") List<Long> categoryIds,
            @Param("numCategories") long numCategories,
            Pageable pageable);
//    ORDER BY p.product_price
}
