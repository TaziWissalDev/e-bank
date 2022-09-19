package org.sid.ebankingbackend.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.ebankingbackend.dtos.*;
import org.sid.ebankingbackend.entities.*;
import org.sid.ebankingbackend.enums.OperationType;
import org.sid.ebankingbackend.exceptions.BalanceNotSufficientException;
import org.sid.ebankingbackend.exceptions.BankAccountNoFoundException;
import org.sid.ebankingbackend.exceptions.CustomerNotFountException;
import org.sid.ebankingbackend.mappers.BankAccountMapperImpl;
import org.sid.ebankingbackend.repositories.AccountOperationRepository;
import org.sid.ebankingbackend.repositories.BankAccountRepository;
import org.sid.ebankingbackend.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService{

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;

    private BankAccountMapperImpl dtomapper;

    // we use lombok as annotation that implement this code automatically
   /* public BankAccountServiceImpl(CustomerRepository customerRepository, BankAccountRepository bankAccountRepository, AccountOperationRepository accountOperationRepository) {
        this.customerRepository = customerRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.accountOperationRepository = accountOperationRepository;
    }
*/

//    Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new customer");
        Customer customer = dtomapper.fromCustomerDTO(customerDTO);
        Customer savedcustomer = customerRepository.save(customer);
        return dtomapper.fromCustomer(savedcustomer);
    }

    @Override
    public CurrentBankAccoountDto saveCurrentAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFountException {

        Customer customer = getCustomerById(customerId);

        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreated_at(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);
        CurrentAccount savedBankAccount = bankAccountRepository.save(currentAccount);
        return dtomapper.fromCurrentBankAccount(savedBankAccount);
    }

    @Override
    public SavingBankAccountDto saveSavingAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFountException {
        Customer customer = getCustomerById(customerId);
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreated_at(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);

        SavingAccount savedBankAccount = bankAccountRepository.save(savingAccount);
        return dtomapper.fromSavingBankAccount(savedBankAccount);
    }

    @Override
    public List<CustomerDTO> getCustomers() {
            List<Customer> customers = customerRepository.findAll();
            //programation fonctionnelle
            List<CustomerDTO> customerDTOS= customers.stream().map(customer -> dtomapper.fromCustomer(customer)).collect(Collectors.toList());
            //programmation impérative
            /*List<CustomerDTO> customerDTOS= new ArrayList<>();
            for(Customer customer:customers){
                CustomerDTO customerDTO =  dtomapper.fromCustomer(customer);
                customerDTOS.add(customerDTO);
            }*/

        return customerDTOS;
    }

    @Override
    public Customer getCustomerById(Long customerId) throws CustomerNotFountException {
         Customer customer = customerRepository.findById(customerId).orElseThrow(()-> new CustomerNotFountException("Customer not found"));
        return customer;
    }

    @Override
    public BankAccountDto getBankAccount(String accountId) throws BankAccountNoFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(
                () -> new BankAccountNoFoundException("Account not found"));

        if(bankAccount instanceof SavingAccount){
            SavingAccount savingAccount  = (SavingAccount) bankAccount;
            return dtomapper.fromSavingBankAccount(savingAccount);
        }else{
            CurrentAccount currentAccount  = (CurrentAccount) bankAccount;
            return dtomapper.fromCurrentBankAccount(currentAccount);
        }
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNoFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNoFoundException("BankAccount not found"));
        if(bankAccount.getBalance() < amount)
            throw  new BalanceNotSufficientException("Balance not sufficient");

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);

        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNoFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNoFoundException ("BankAccount not found"));
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);

        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BalanceNotSufficientException, BankAccountNoFoundException {
        debit(accountIdSource,amount,"tranfer to "+accountIdDestination);
        credit(accountIdDestination,amount,"transfer from "+accountIdSource);
    }

    @Override
    public List<BankAccountDto> bankAccountList(){
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        List<BankAccountDto> bankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return dtomapper.fromSavingBankAccount(savingAccount);
            } else {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return dtomapper.fromCurrentBankAccount(currentAccount);
            }
        }).collect(Collectors.toList());
        return bankAccountDTOS;
    }

    @Override
    public CustomerDTO getCustomer(Long customerID) throws CustomerNotFountException {
       Customer customer = customerRepository.findById(customerID)
                .orElseThrow(()->new CustomerNotFountException("Customer not found"));
        return dtomapper.fromCustomer(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Saving new customer");
        Customer customer = dtomapper.fromCustomerDTO(customerDTO);
        Customer savedcustomer = customerRepository.save(customer);
        return dtomapper.fromCustomer(savedcustomer);
    }

    @Override
    public void deleteCustomer(Long customerID){
        customerRepository.deleteById(customerID);
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId){
      List<AccountOperation> accountOperations =  accountOperationRepository.findByBankAccountId(accountId);
      return accountOperations.stream().map( op ->dtomapper.fromAccountOperation(op)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNoFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
        if(bankAccount == null) throw new BankAccountNoFoundException("Account not found!");
        Page<AccountOperation> accountOperations =  accountOperationRepository.findByBankAccountIdOrderByOperationDateDesc(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS =  accountOperations.getContent().stream().map(op -> dtomapper.fromAccountOperation(op)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDTO;
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        List<Customer> customers=customerRepository.searchCustomer(keyword);
        List<CustomerDTO> customerDTOS = customers.stream().map(cust -> dtomapper.fromCustomer(cust)).collect(Collectors.toList());
        return customerDTOS;
    }

    @Override
    public List<BankAccount> getBankAccountByCustomerId(Long customerId) {
        List<BankAccount> bankAccounts = bankAccountRepository.findBankAccountByCustomerId(customerId);
        return bankAccounts;
    }

}
