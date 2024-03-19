package com.oleksa.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_sku")
    private String sku;

    @Column(name = "product_name")
    private String name;

    @Column(name = "product_description")
    private String description;

    @Column(name = "product_price")
    private Double price;

    @Column(name = "product_image_url")
    private String imageUrl;

    @Column(name = "product_active")
    private boolean active;

    @Column(name = "product_quantity")
    private int unitsInStock;

    @Column(name = "product_date_created")
    @CreationTimestamp
    private Date dateCreated;

    @Column(name = "product_last_updated")
    @UpdateTimestamp
    private Date lastUpdated;

    @ElementCollection
    @CollectionTable(name = "product_has_category", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "category_id")
    private List<Long> categoryIds;

}
