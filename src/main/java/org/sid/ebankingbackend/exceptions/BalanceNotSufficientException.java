package org.sid.ebankingbackend.exceptions;

public class BalanceNotSufficientException extends Exception {
    public BalanceNotSufficientException(String balance_not_sufficient) {
        super(balance_not_sufficient);
    }
}
