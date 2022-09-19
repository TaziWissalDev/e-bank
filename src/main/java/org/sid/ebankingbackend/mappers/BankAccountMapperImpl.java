package org.sid.ebankingbackend.mappers;

import org.sid.ebankingbackend.dtos.AccountOperationDTO;
import org.sid.ebankingbackend.dtos.CurrentBankAccoountDto;
import org.sid.ebankingbackend.dtos.CustomerDTO;
import org.sid.ebankingbackend.dtos.SavingBankAccountDto;
import org.sid.ebankingbackend.entities.AccountOperation;
import org.sid.ebankingbackend.entities.CurrentAccount;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImpl {
    public CustomerDTO fromCustomer(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer,customerDTO);
       /* customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setEmail(customer.getEmail());*/
        return customerDTO;
    }

    public Customer fromCustomerDTO(CustomerDTO customerDTO){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO,customer);
        return customer;
    }

    public SavingBankAccountDto fromSavingBankAccount(SavingAccount savingAccount){
        SavingBankAccountDto savingBankAccountDto = new SavingBankAccountDto();
        BeanUtils.copyProperties(savingAccount,savingBankAccountDto);
        savingBankAccountDto.setCustomerDto(fromCustomer(savingAccount.getCustomer()));
        savingBankAccountDto.setType(savingAccount.getClass().getSimpleName());
        return savingBankAccountDto;

    }

    public SavingAccount fromSavingBankAccountDto(SavingBankAccountDto savingBankAccountDto){
        SavingAccount savingBankAccount = new SavingAccount();
        BeanUtils.copyProperties(savingBankAccountDto,savingBankAccount);
        savingBankAccount.setCustomer(fromCustomerDTO(savingBankAccountDto.getCustomerDto()));
        return savingBankAccount;
    }

    public CurrentBankAccoountDto fromCurrentBankAccount(CurrentAccount currentAccount){
        CurrentBankAccoountDto currentBankAccoountDto = new CurrentBankAccoountDto();
        BeanUtils.copyProperties(currentAccount,currentBankAccoountDto);
        currentBankAccoountDto.setCustomerDto(fromCustomer(currentAccount.getCustomer()));
        currentBankAccoountDto.setType(currentAccount.getClass().getSimpleName());
        return currentBankAccoountDto;

    }

    public CurrentAccount fromCurrentBankAccountDto(CurrentBankAccoountDto currentBankAccoountDto){
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(currentBankAccoountDto,currentAccount);
        currentAccount.setCustomer(fromCustomerDTO(currentBankAccoountDto.getCustomerDto()));
        return currentAccount;
    }

    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation){
        AccountOperationDTO accountOperationDTO = new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation,accountOperationDTO);
        return accountOperationDTO;
    }

    public AccountOperation fromAccountOperationDto(AccountOperationDTO accountOperationDto){
        AccountOperation accountOperation = new AccountOperation();
        BeanUtils.copyProperties(accountOperationDto,accountOperation);
        return accountOperation;
    }
}
