package com.example.hexagonal_bank.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type" ,discriminatorType = DiscriminatorType.STRING,length = 4)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BankAccount {
    @Id
    protected String id;
    protected double balance;
    protected Date creationDate;
    @Column(name = "type",insertable = false,updatable = false)
    protected String type;
    @Column(name = "account_number")
    protected String accountNumber;
    @ManyToOne
    protected Customer customer;
    @OneToMany(mappedBy = "bankAccount",fetch = FetchType.LAZY)
    protected List<AccountOperation> accountOperations;

    public String getType(){
        return type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setAccountOperations(List<AccountOperation> accountOperations) {
        this.accountOperations = accountOperations;
    }
}
