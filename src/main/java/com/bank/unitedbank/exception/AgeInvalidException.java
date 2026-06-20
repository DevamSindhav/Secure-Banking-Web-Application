package com.bank.unitedbank.exception;

import java.lang.RuntimeException;

public class AgeInvalidException extends RuntimeException {

    public AgeInvalidException(String message){
        super(message);
    }

}
