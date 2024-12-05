package com.example.hexagonal_bank.controller;

import com.example.hexagonal_bank.dtos.BankAccountDTO;
import com.example.hexagonal_bank.dtos.CreateBankAccountDTO;
import com.example.hexagonal_bank.dtos.CreditDTO;
import com.example.hexagonal_bank.dtos.DebitDTO;
import com.example.hexagonal_bank.exceptions.BalanceNotSufficientException;
import com.example.hexagonal_bank.exceptions.BankAccountNotFoundException;
import com.example.hexagonal_bank.exceptions.CustomerNotFoundException;
import com.example.hexagonal_bank.model.BankAccount;
import com.example.hexagonal_bank.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/bank")
public class BankAccountController {
    private BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PostMapping("/create-bank-account")
    public void createCustomerBankAccount(@RequestBody CreateBankAccountDTO createBankAccountDTO) throws CustomerNotFoundException {
         bankAccountService.createNewBankAccount(createBankAccountDTO);
    }

    @GetMapping("/list-account/{id}")
    public List<BankAccountDTO> getBankList(@PathVariable Long id){
        return bankAccountService.bankAccountList(id);
    }

    @PostMapping("/debit-account")
    public DebitDTO debitAccount(@RequestBody DebitDTO debitDTO)throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.debit(debitDTO.getAccountId(),debitDTO.getAmount(),debitDTO.getDescription());
        return debitDTO;
    }

    @PostMapping("/credit-account")
    public CreditDTO creditAccount(@RequestBody CreditDTO creditDTO)throws BankAccountNotFoundException{
        this.bankAccountService.credit(creditDTO.getAccountId(),creditDTO.getAmount(),creditDTO.getDescription());
        return creditDTO;
    }
}
