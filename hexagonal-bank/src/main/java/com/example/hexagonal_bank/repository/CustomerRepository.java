package com.example.hexagonal_bank.repository;

import com.example.hexagonal_bank.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
}
