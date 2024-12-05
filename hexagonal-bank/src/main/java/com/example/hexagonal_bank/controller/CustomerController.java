package com.example.hexagonal_bank.controller;

import com.example.hexagonal_bank.dtos.CustomerDTO;
import com.example.hexagonal_bank.model.Customer;
import com.example.hexagonal_bank.repository.CustomerRepository;
import com.example.hexagonal_bank.services.BankAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Customer Management", description = "Endpoints for managing customers")
@Slf4j
@CrossOrigin("*")
public class CustomerController {

    private BankAccountService bankAccountService;
    private CustomerRepository customerRepository;

    public CustomerController(BankAccountService bankAccountService, CustomerRepository customerRepository) {
        this.bankAccountService = bankAccountService;
        this.customerRepository = customerRepository;
    }

    @Operation(summary = "Get all customers")
    @GetMapping("/customers")
    public List<CustomerDTO> getAllCustomers(){
        return bankAccountService.listCustomers();
    }


    @Operation(summary = "Create a new customer")
    @PostMapping("/customers")
    public CustomerDTO createCustomer(@RequestBody CustomerDTO customerDTO){
        return bankAccountService.saveCustomer(customerDTO);
    }
}
