package org.sid.ebankingbackend.services;

import org.sid.ebankingbackend.dtos.*;
import org.sid.ebankingbackend.entities.BankAccount;
import org.sid.ebankingbackend.entities.CurrentAccount;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.entities.SavingAccount;
import org.sid.ebankingbackend.exceptions.BalanceNotSufficientException;
import org.sid.ebankingbackend.exceptions.BankAccountNoFoundException;
import org.sid.ebankingbackend.exceptions.CustomerNotFountException;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customer);
    CurrentBankAccoountDto saveCurrentAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFountException;
    SavingBankAccountDto saveSavingAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFountException;

    List<CustomerDTO> getCustomers();
    Customer getCustomerById(Long customerId) throws CustomerNotFountException;
    BankAccountDto getBankAccount(String accountId) throws BankAccountNoFoundException;

    void debit(String accountId, double amount,String description) throws BankAccountNoFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount,String description) throws BankAccountNoFoundException;
    void transfer(String accountIdSource,String accountIdDestination,double amount) throws BalanceNotSufficientException, BankAccountNoFoundException;

    List<BankAccountDto> bankAccountList();

    CustomerDTO getCustomer(Long customerID) throws CustomerNotFountException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerID);

    List<AccountOperationDTO> accountHistory(String accountId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNoFoundException;

    List<CustomerDTO> searchCustomers(String s);

    List<BankAccount> getBankAccountByCustomerId(Long customerId);
}
