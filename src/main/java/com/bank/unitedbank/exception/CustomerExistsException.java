package com.bank.unitedbank.exception;

public class CustomerExistsException extends RuntimeException {
    public CustomerExistsException(String message){
        super(message);
    }
}
