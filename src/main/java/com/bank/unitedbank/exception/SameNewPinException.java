package com.bank.unitedbank.exception;

public class SameNewPinException extends RuntimeException{
    public SameNewPinException(String message){
       super(message);
    }
}
