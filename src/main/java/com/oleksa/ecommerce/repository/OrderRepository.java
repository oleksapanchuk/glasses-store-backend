package com.oleksa.ecommerce.repository;

import com.oleksa.ecommerce.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderTrackingNumber(@Param("trackingNumber") String trackingNumber);

    Page<Order> findByUserUsernameOrderByDateCreatedDesc(@Param("username") String username, Pageable pageable);
}
