package org.sid.ebankingbackend;

import org.sid.ebankingbackend.entities.*;
import org.sid.ebankingbackend.enums.AccountStatus;
import org.sid.ebankingbackend.enums.OperationType;
import org.sid.ebankingbackend.exceptions.CustomerNotFountException;
import org.sid.ebankingbackend.repositories.AccountOperationRepository;
import org.sid.ebankingbackend.repositories.BankAccountRepository;
import org.sid.ebankingbackend.repositories.CustomerRepository;
import org.sid.ebankingbackend.services.BankAccountService;
import org.sid.ebankingbackend.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);



    }

//    @Bean
    CommandLineRunner start(CustomerRepository customerRepository, BankAccountRepository bankAccountRepository, AccountOperationRepository accountOperationRepository){
        return args -> {
            Stream.of("hassan","yassin","wissal").forEach(name ->{
                Customer c = new Customer();
                c.setEmail(name+"@gmail.com");
                c.setName(name);
                customerRepository.save(c);

            });

            customerRepository.findAll().forEach(customer ->{
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setCustomer(customer);
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setBalance(Math.random()*90000);
                currentAccount.setCreated_at(new Date());
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);


                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setCustomer(customer);
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setBalance(Math.random()*90000);
                savingAccount.setCreated_at(new Date());
                savingAccount.setInterestRate(5.5);
                bankAccountRepository.save(savingAccount);
            });

            bankAccountRepository.findAll().forEach(acc ->{
                for (int i=0;i<10;i++){
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random()*1200);
                    accountOperation.setType(Math.random() > 0.5 ?  OperationType.DEBIT : OperationType.CREDIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);
                }

            });




        };
    }

    @Bean
    CommandLineRunner commandLineRunner(BankService bankService, BankAccountService bankAccountService) {
            return args -> {
                   // bankService.consulter();

                Stream.of("hassan","yassin","wissal").forEach(name ->{
                    Customer c = new Customer();
                    c.setEmail(name+"@gmail.com");
                    c.setName(name);
                    bankAccountService.saveCustomer(c);

                });

                bankAccountService.getCustomers().forEach(customer ->{
                    try {
                        bankAccountService.saveCurrentAccount(Math.random()*90000,90000,customer.getId());
                        bankAccountService.saveSavingAccount(Math.random()*120000,5.5,customer.getId());
                        bankAccountService.bankAccountList().forEach( acc ->{

                        });
                    } catch (CustomerNotFountException e) {
                        e.printStackTrace();
                    }


                });



            };
    }
    }
