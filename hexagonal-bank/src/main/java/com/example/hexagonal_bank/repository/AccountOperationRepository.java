package com.example.hexagonal_bank.repository;

import com.example.hexagonal_bank.model.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepository extends JpaRepository<AccountOperation,Long> {
}
