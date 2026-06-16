package com.bank.unitedbank.service;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bank.unitedbank.repository.AccountRepository;
import com.bank.unitedbank.entity.Account;


import java.math.BigDecimal;

@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository ,
                          PasswordEncoder passwordEncoder
    ){
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Account createAccount(Account account ){

        account.setPinHashed(passwordEncoder.encode(account.getPinHashed()));

        account.setBalance(new BigDecimal("0.0"));

        return accountRepository.save(account);
    }

    public void updatePin(Long accNo , String newPlainPin ){

        Account account = getAccountById(accNo);

        account.setPinHashed(passwordEncoder.encode(newPlainPin));

        accountRepository.save(account);
    }

    public void updateBalance(Account account , BigDecimal newBalance){

        account.setBalance(newBalance);
        accountRepository.save(account);
    }

    public boolean hasActiveAccount(Long customerId){

        return accountRepository.existsByCustomerCustomerId(customerId);

    }

    public void deleteAccount(Long accNo){

        Account account = getAccountById(accNo);

        if(!isBalanceZero(account)){
            throw new RuntimeException("Balance not Zero");
        }
        account.setIsAccountValid(false);

        accountRepository.save(account);
    }

    //Helper methods
    public boolean isPinCorrect(Account account , String plainPin){

        return passwordEncoder.matches(
                plainPin,
                account.getPinHashed()
        );
    }

    public boolean isBalanceZero(Account account){
        return account.getBalance()
                .compareTo(new BigDecimal("0.0")) == 0;
    }

    public Account getAccountById(Long accNo){
        return accountRepository.findById(accNo)
                .orElseThrow( () -> new RuntimeException("Account not found"));
    }

}
