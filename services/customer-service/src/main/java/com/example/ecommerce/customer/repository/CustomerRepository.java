package com.example.ecommerce.customer.repository;


import com.example.ecommerce.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
