package com.example.hexagonal_bank.services;

import com.example.hexagonal_bank.dtos.*;
import com.example.hexagonal_bank.enums.OperationType;
import com.example.hexagonal_bank.exceptions.BalanceNotSufficientException;
import com.example.hexagonal_bank.exceptions.BankAccountNotFoundException;
import com.example.hexagonal_bank.exceptions.CustomerNotFoundException;
import com.example.hexagonal_bank.mappers.BankAccountMapper;
import com.example.hexagonal_bank.model.*;
import com.example.hexagonal_bank.repository.AccountOperationRepository;
import com.example.hexagonal_bank.repository.BankAccountRepository;
import com.example.hexagonal_bank.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Data
public class BankAccountServiceImpl implements BankAccountService{
    private String accountNumber=null;

    private static final Logger log = LoggerFactory.getLogger(BankAccountServiceImpl.class);
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private BankAccountMapper dtoMapper;
    private AccountOperationRepository accountOperationRepository;

    public BankAccountServiceImpl(CustomerRepository customerRepository, BankAccountRepository bankAccountRepository, BankAccountMapper dtoMapper, AccountOperationRepository accountOperationRepository) {
        this.customerRepository = customerRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.dtoMapper = dtoMapper;
        this.accountOperationRepository = accountOperationRepository;
        this.accountNumber = buildNewAccount();
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer");
        Customer customer=dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    public String buildNewAccount(){
        return "FR76"+(int)(Math.random()*100000);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId,boolean isTwoAccounts) throws CustomerNotFoundException {
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if(customer==null)
            throw new CustomerNotFoundException("Customer not found");
        CurrentAccount currentAccount=new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreationDate(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setAccountNumber(isTwoAccounts?this.accountNumber:buildNewAccount());
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);
        CurrentAccount savedBankAccount = bankAccountRepository.save(currentAccount);
        return dtoMapper.fromCurrentBankAccount(savedBankAccount);
    }

    @Override
    public SavingAccountDTO saveSavingBankAccount(double initialBalance, double depositLimit, Long customerId,boolean isTwoAccounts) throws CustomerNotFoundException {
        Customer customer=customerRepository.findById(customerId).orElse(null);
        String numberAccount = "FR76"+(int)(Math.random()*100000);
        if(customer==null)
            throw new CustomerNotFoundException("Customer not found");
        SavingAccount savingAccount=new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreationDate(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setAccountNumber(isTwoAccounts?this.accountNumber:buildNewAccount());
        savingAccount.setDepositLimit(depositLimit);
        savingAccount.setCustomer(customer);
        SavingAccount savedBankAccount = bankAccountRepository.save(savingAccount);
        return dtoMapper.fromSavingAccount(savedBankAccount);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(customer -> dtoMapper.fromCustomer(customer))
                .collect(Collectors.toList());
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));
        if(bankAccount instanceof SavingAccount){
            SavingAccount savingAccount= (SavingAccount) bankAccount;
            return dtoMapper.fromSavingAccount(savingAccount);
        } else {
            CurrentAccount currentAccount= (CurrentAccount) bankAccount;
            return dtoMapper.fromCurrentBankAccount(currentAccount);
        }
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));
        if(bankAccount.getBalance()<amount)
            throw new BalanceNotSufficientException("Balance not sufficient");
        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));
        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource,amount,"Transfer to "+accountIdDestination);
        credit(accountIdDestination,amount,"Transfer from "+accountIdSource);
    }

    @Override
    public List<BankAccountDTO> bankAccountList(Long id) {
        return bankAccountRepository.findByCustomerId(id).get().stream()
                .map( bankAccount -> {
                    if (bankAccount instanceof CurrentAccount){
                      return   dtoMapper.fromCurrentBankAccount((CurrentAccount) bankAccount);
                    } else { return dtoMapper.fromSavingAccount((SavingAccount) bankAccount);}
                }).collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        return dtoMapper.fromCustomer(customerRepository.findById(customerId).get());
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        return null;
    }

    @Override
    public void deleteCustomer(Long customerId) {

    }

    @Override
    public void createNewBankAccount(CreateBankAccountDTO createBankAccountDTO) throws CustomerNotFoundException {
        if (createBankAccountDTO.isNeedTwoAccounts()){
            saveCurrentBankAccount(createBankAccountDTO.getInitialBalance(),createBankAccountDTO.getOverDraft(),createBankAccountDTO.getCustomerId(),createBankAccountDTO.isNeedTwoAccounts());
            saveSavingBankAccount(createBankAccountDTO.getInitialBalance(),createBankAccountDTO.getDepositLimit(),createBankAccountDTO.getCustomerId(),createBankAccountDTO.isNeedTwoAccounts());
        } else {
            // to refer CurrentAccount if type start with character c
            if (createBankAccountDTO.getType().startsWith("C")){
                saveCurrentBankAccount(createBankAccountDTO.getInitialBalance(),createBankAccountDTO.getOverDraft(),createBankAccountDTO.getCustomerId(),createBankAccountDTO.isNeedTwoAccounts());
            } else {
                saveSavingBankAccount(createBankAccountDTO.getInitialBalance(),createBankAccountDTO.getDepositLimit(),createBankAccountDTO.getCustomerId(),createBankAccountDTO.isNeedTwoAccounts());
            }
        }
    }
}
