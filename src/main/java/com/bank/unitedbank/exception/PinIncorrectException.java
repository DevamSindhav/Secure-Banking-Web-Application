package com.bank.unitedbank.exception;

public class PinIncorrectException extends RuntimeException{
    public PinIncorrectException(String message){
        super(message);
    }
}
