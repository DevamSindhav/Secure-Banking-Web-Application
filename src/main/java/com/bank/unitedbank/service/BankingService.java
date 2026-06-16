package com.bank.unitedbank.service;

import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.bank.unitedbank.entity.Account;

import java.math.BigDecimal;

@Service
@Transactional
public class BankingService {

    private final AccountService accountService;
    private final TransactionService transactionService;

    public BankingService(AccountService accountService,
                          TransactionService transactionService
    ){
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    public void deposit(Long accNo , BigDecimal amount){

        if(amount.compareTo(new BigDecimal("0.0")) <= 0){
            throw new RuntimeException("Deposit amount can not be 0");
        }

        Account account = accountService.getAccountById(accNo);

        accountService.updateBalance(account , account.getBalance().add(amount));

        transactionService.recordTransaction(account , amount , "DEPOSIT_");
    }

    public void withdraw(Long accNo , String plainPin , BigDecimal amount ){
        if(amount.compareTo(new BigDecimal("0.0")) <= 0){
            throw new RuntimeException("Withdraw amount can not be 0");
        }

        Account account = accountService.getAccountById(accNo);

        if(!accountService.isPinCorrect(account , plainPin)){
            throw new RuntimeException("Pin is incorrect");
        }

        BigDecimal currentBalance = account.getBalance();

        if(currentBalance.compareTo(amount) < 0){
            throw new RuntimeException("Not enough Balance");
        }


        accountService.updateBalance(account , currentBalance.subtract(amount));

        transactionService.recordTransaction(account , amount , "WITHDRAW_");

    }

    public void transfer(Long senderAccNo , Long receiverAccNo , String plainPin , BigDecimal amount){

        if(amount.compareTo(new BigDecimal("0.0")) <= 0){
            throw new RuntimeException("Transfer amount can not be 0");
        }

        Account senderAccount = accountService.getAccountById(senderAccNo);

        if(!accountService.isPinCorrect(senderAccount , plainPin)){
            throw new RuntimeException("Pin is incorrect");
        }

        BigDecimal currentBalance = senderAccount.getBalance();

        if(currentBalance.compareTo(amount) < 0){
            throw new RuntimeException("Not enough Balance");
        }

        Account receiverAccount = accountService.getAccountById(receiverAccNo);

        accountService.updateBalance(senderAccount , senderAccount.getBalance().subtract(amount));
        accountService.updateBalance(receiverAccount , receiverAccount.getBalance().add(amount));

        transactionService.recordTransaction(senderAccount , amount , "TRANSFER_TO_" + receiverAccNo.toString());
        transactionService.recordTransaction(receiverAccount , amount , "TRANSFER_FROM_" + senderAccNo.toString());

    }
}
