package com.example.hexagonal_bank.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor @AllArgsConstructor
public class SavingAccountDTO extends BankAccountDTO{
    private String id;
    private double balance;
    private Date creationDate;
    private CustomerDTO customerDTO;
    private double depositLimit;

    public SavingAccountDTO(String type) {
        super(type);
    }
}
