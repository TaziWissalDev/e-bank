package org.sid.ebankingbackend.dtos;

import lombok.Data;
import org.sid.ebankingbackend.enums.AccountStatus;

import java.util.Date;

@Data
public class CurrentBankAccoountDto extends BankAccountDto{
    private String id;
    private double balance;
    private Date created_at;
    private AccountStatus status;
    private CustomerDTO customerDto;
    private double overDraft;
}
