package com.example.hexagonal_bank;

import com.example.hexagonal_bank.enums.OperationType;
import com.example.hexagonal_bank.model.AccountOperation;
import com.example.hexagonal_bank.model.CurrentAccount;
import com.example.hexagonal_bank.model.Customer;
import com.example.hexagonal_bank.model.SavingAccount;
import com.example.hexagonal_bank.repository.AccountOperationRepository;
import com.example.hexagonal_bank.repository.BankAccountRepository;
import com.example.hexagonal_bank.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;


@SpringBootApplication
public class HexagonalBankApplication {


	public static void main(String[] args) {
		SpringApplication.run(HexagonalBankApplication.class, args);
	}

	@Bean
	CommandLineRunner start(CustomerRepository customerRepository,
							BankAccountRepository bankAccountRepository,
							AccountOperationRepository accountOperationRepository){
		return args -> {
			Stream.of("Phenril","Agun","Aicha").forEach(name->{
				Customer customer=new Customer();
				customer.setName(name);
				customer.setEmail(name+"@gmail.com");
				customerRepository.save(customer);
			});
			customerRepository.findAll().forEach(cust->{
				String numberAccount = "FR76"+(int)(Math.random()*100000);
				CurrentAccount currentAccount=new CurrentAccount();
				currentAccount.setId(UUID.randomUUID().toString());
				currentAccount.setBalance(Math.random()*90000);
				currentAccount.setCreationDate(new Date());
				currentAccount.setCustomer(cust);
				currentAccount.setAccountNumber(numberAccount);
				currentAccount.setOverDraft(9000);
				bankAccountRepository.save(currentAccount);

				SavingAccount savingAccount=new SavingAccount();
				savingAccount.setId(UUID.randomUUID().toString());
				savingAccount.setBalance(Math.random()*90000);
				savingAccount.setCreationDate(new Date());
				savingAccount.setCustomer(cust);
				savingAccount.setAccountNumber(numberAccount);
				savingAccount.setDepositLimit(5.5);
				bankAccountRepository.save(savingAccount);

			});
			bankAccountRepository.findAll().forEach(acc->{
				for (int i = 0; i <10 ; i++) {
					AccountOperation accountOperation=new AccountOperation();
					accountOperation.setOperationDate(new Date());
					accountOperation.setAmount(Math.random()*12000);
					accountOperation.setType(Math.random()>0.5? OperationType.DEBIT: OperationType.CREDIT);
					accountOperation.setBankAccount(acc);
					accountOperationRepository.save(accountOperation);
				}

			});
		};

	}
}
