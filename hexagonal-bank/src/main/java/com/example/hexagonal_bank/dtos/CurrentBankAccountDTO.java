package com.example.hexagonal_bank.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
public class CurrentBankAccountDTO extends BankAccountDTO{
    private String id;
    private double balance;
    private Date creationDate;
    private CustomerDTO customerDTO;
    private double overDraft;

    public CurrentBankAccountDTO(String type) {
        super(type);
    }
}
