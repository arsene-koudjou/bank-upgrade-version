package com.example.hexagonal_bank.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBankAccountDTO {
    private Long customerId;
    private String type;
    private double initialBalance;
    private double overDraft;
    private double depositLimit;
    private boolean needTwoAccounts;

    public boolean isNeedTwoAccounts() {
        return needTwoAccounts;
    }
}
