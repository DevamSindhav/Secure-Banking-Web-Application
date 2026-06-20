package com.bank.unitedbank.exception;

public class BalanceNotZeroException extends RuntimeException{
    public BalanceNotZeroException(String message){
        super(message);
    }
}
