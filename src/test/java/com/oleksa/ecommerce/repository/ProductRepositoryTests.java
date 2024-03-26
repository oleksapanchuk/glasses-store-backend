package com.oleksa.ecommerce.repository;

import com.oleksa.ecommerce.entity.Product;
import com.oleksa.ecommerce.entity.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    public void setupTestData() {
        product = Product.builder()
                .id(1L)
                .sku("sku001")
                .name("test_name")
                .description("test_description")
                .price(26.03)
                .active(true)
                .unitsInStock(111)
                .imageUrl("test_url")
                .build();
    }

    @Test
    public void whenFindByNameContaining_thenReturnProducts() {
        // given
        Product product = new Product();
        product.setName("testProduct");
        entityManager.persist(product);
        entityManager.flush();

        // when
        Page<Product> found = productRepository.findByNameContaining(product.getName(), PageRequest.of(0, 5));

        // then
        assertThat(found.getContent()).hasSize(1);
        assertThat(found.getContent().get(0).getName()).isEqualTo(product.getName());
    }

    @Test
    public void whenFindProductsByPriceRange_thenReturnProducts() {
        // given
        Product product1 = new Product();
        product1.setPrice(50.0);
        entityManager.persist(product1);

        Product product2 = new Product();
        product2.setPrice(150.0);
        entityManager.persist(product2);

        entityManager.flush();

        // when
        Page<Product> found = productRepository.findProductsByPriceRange(40.0, 160.0, PageRequest.of(0, 5));

        found.getContent().forEach((product3) -> System.out.println(product3.getName()));

        // then
        assertThat(found.getContent()).hasSize(2);
        assertThat(found.getContent()).contains(product1, product2);
    }

    @Test
    public void whenFindProductsByCategoriesAndPriceRange_thenReturnProducts() {
        // given
        ProductCategory category1 = new ProductCategory();
        entityManager.persist(category1);

        ProductCategory category2 = new ProductCategory();
        entityManager.persist(category2);

        Product product1 = new Product();
        product1.setPrice(50.0);
        product1.setCategoryIds(Arrays.asList(category1.getId()));
        entityManager.persist(product1);

        Product product2 = new Product();
        product2.setPrice(150.0);
        product2.setCategoryIds(Arrays.asList(category1.getId(), category2.getId()));
        entityManager.persist(product2);

        entityManager.flush();

        // when
        Page<Product> found = productRepository.findProductsByCategoriesAndPriceRange(
                40.0, 160.0, Arrays.asList(category1.getId(), category2.getId()), 2L, PageRequest.of(0, 5));

        // then
        assertThat(found.getContent()).hasSize(1);
        assertThat(found.getContent().get(0)).isEqualTo(product2);
    }

}
