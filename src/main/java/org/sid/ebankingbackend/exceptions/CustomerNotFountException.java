package org.sid.ebankingbackend.exceptions;

public class CustomerNotFountException extends Exception {
    // runtimeException for not monitor exception
    public CustomerNotFountException(String customer_not_found) {
        super(customer_not_found);
    }
}
