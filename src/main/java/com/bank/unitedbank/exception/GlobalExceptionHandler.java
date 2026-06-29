package com.bank.unitedbank.exception;

import com.bank.unitedbank.dto.response.ErrorResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.Instant;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {
            AgeInvalidException.class,
            BalanceNotZeroException.class,
            IniBalanceInvalidException.class,
            InsufficientBalanceException.class,
            ZeroAmountException.class,
            TransferToSelfException.class
    })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse badRequestException(RuntimeException exception){

        return new ErrorResponse(
                400,
                "BAD_REQUEST",
                exception.getMessage(),
                Instant.now()
        );
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse methodArgumetNotValidException(MethodArgumentNotValidException exception){

        String message =   exception.getBindingResult()
                                    .getFieldErrors()
                                    .getFirst().getDefaultMessage();

        return new ErrorResponse(
                400,
                "VALIDATION_ERROR",
                message,
                Instant.now()
        );
    }

    @ExceptionHandler(value = {
            UnauthorizedAccountException.class,
            PasswordIncorrectException.class,
            PinIncorrectException.class
    })
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorResponse unauthorizedAccessException(RuntimeException exception){

        return new ErrorResponse(
                401,
                "UNAUTHORIZED",
                exception.getMessage(),
                Instant.now()
        );
    }

    @ExceptionHandler(value = {
            AccountNotFoundException.class ,
            CustomerNotFoundException.class
    })
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorResponse resourceNotFoundException(RuntimeException exception){

        return  new ErrorResponse(
                404,
                "NOT_FOUND",
                exception.getMessage(),
                Instant.now()
        );
    }

    @ExceptionHandler(value = {
            CustomerExistsException.class,
            ActiveAccountException.class,
            SameNewPinException.class,
            SameNewPasswordException.class
    })
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorResponse resourceFoundException(RuntimeException exception){

        return new ErrorResponse(
                409,
                "CONFLICT",
                exception.getMessage(),
                Instant.now()
        );
    }

    //catch all Exception handling
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse unexpectedError(Exception exception){

        return new ErrorResponse(
                500,
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred :" + exception.getMessage(),
                Instant.now()
        );
    }

}

