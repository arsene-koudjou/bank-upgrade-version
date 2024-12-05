package com.example.hexagonal_bank.repository;

import com.example.hexagonal_bank.model.BankAccount;
import io.micrometer.common.KeyValues;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
    Optional<List<BankAccount>> findByCustomerId(Long customerId);
}
