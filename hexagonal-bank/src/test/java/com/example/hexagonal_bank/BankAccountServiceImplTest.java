package com.example.hexagonal_bank;

import com.example.hexagonal_bank.dtos.CustomerDTO;
import com.example.hexagonal_bank.exceptions.CustomerNotFoundException;
import com.example.hexagonal_bank.mappers.BankAccountMapper;
import com.example.hexagonal_bank.model.Customer;
import com.example.hexagonal_bank.repository.CustomerRepository;
import com.example.hexagonal_bank.services.BankAccountServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BankAccountServiceImplTest {

    @MockBean
    private CustomerRepository customerRepository;
    @MockBean
    private BankAccountServiceImpl bankAccountService;
    @MockBean
    private BankAccountMapper bankAccountMapper;


    @BeforeEach
    void setUp() {

    }

    @Test
    void getCustomerById_shouldReturnUserIfExists() throws CustomerNotFoundException {

        Customer customer = new Customer(1L,"John Doe","john.doe@example.com");

        Optional<Customer> resultRepository = customerRepository.findById(1L);
        assertNotNull(resultRepository);
    }

}
