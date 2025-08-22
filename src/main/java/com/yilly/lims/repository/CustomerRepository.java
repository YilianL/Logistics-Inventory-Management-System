package com.yilly.lims.repository;

import com.yilly.lims.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    Optional<Customer> findByCustomerName(String customerName);

    boolean existsByCustomerName(String customerName);
}