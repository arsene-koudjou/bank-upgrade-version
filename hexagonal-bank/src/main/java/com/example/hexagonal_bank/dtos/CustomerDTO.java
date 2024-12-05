package com.example.hexagonal_bank.dtos;

import com.example.hexagonal_bank.model.BankAccount;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor @NoArgsConstructor @Builder
public class CustomerDTO {
    private Long id;
    private String name;
    private String email;
    @JsonIgnore
    private List<BankAccount> bankAccounts;
}
