package org.sid.ebankingbackend.services;

import org.sid.ebankingbackend.entities.BankAccount;
import org.sid.ebankingbackend.entities.CurrentAccount;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.entities.SavingAccount;
import org.sid.ebankingbackend.exceptions.BalanceNotSufficientException;
import org.sid.ebankingbackend.exceptions.BankAccountNoFoundException;
import org.sid.ebankingbackend.exceptions.CustomerNotFountException;

import java.util.List;

public interface BankAccountService {
    Customer saveCustomer(Customer customer);
    CurrentAccount saveCurrentAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFountException;
    SavingAccount saveSavingAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFountException;

    List<Customer> getCustomers();
    Customer getCustomerById(Long customerId) throws CustomerNotFountException;
    BankAccount getBankAccount(String accountId) throws BankAccountNoFoundException;

    void debit(String accountId, double amount,String description) throws BankAccountNoFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount,String description) throws BankAccountNoFoundException;
    void transfer(String accountIdSource,String accountIdDestination,double amount) throws BalanceNotSufficientException, BankAccountNoFoundException;

    List<BankAccount> bankAccountList();
}
