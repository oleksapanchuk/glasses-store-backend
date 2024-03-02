package com.oleksa.ecommerce.repository;

import com.oleksa.ecommerce.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Users, Long> {

    Users findByEmail(String email);
}
