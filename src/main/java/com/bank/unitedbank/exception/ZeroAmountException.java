package com.bank.unitedbank.exception;

public class ZeroAmountException extends RuntimeException{
    public ZeroAmountException(String message){
        super(message);
    }
}
