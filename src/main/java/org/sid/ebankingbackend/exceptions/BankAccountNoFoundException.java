package org.sid.ebankingbackend.exceptions;

public class BankAccountNoFoundException extends Exception {
    public BankAccountNoFoundException(String message) {
        super(message);
    }
}
