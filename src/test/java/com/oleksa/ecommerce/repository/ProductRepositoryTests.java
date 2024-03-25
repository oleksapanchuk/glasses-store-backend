package com.oleksa.ecommerce.repository;

import com.oleksa.ecommerce.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    public void setupTestData(){
        // Given : Setup object or precondition
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

    // JUnit Test for save employee operation
    @Test
    @DisplayName("JUnit test for save product operation")
    public void givenEmployeeObject_whenSave_thenReturnSaveEmployee(){

        // When : Action of behavious that we are going to test
        Product saveProduct = productRepository.save(product);

        // Then : Verify the output
        assertThat(saveProduct).isNotNull();
        assertThat(saveProduct.getId()).isGreaterThan(0);
    }

}
