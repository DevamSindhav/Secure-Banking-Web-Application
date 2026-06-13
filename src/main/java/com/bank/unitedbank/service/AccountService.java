package com.bank.unitedbank.service;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bank.unitedbank.repository.AccountRepository;
import com.bank.unitedbank.entity.Customer;
import com.bank.unitedbank.entity.Account;


import java.math.BigDecimal;

@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final BankingService bankingService;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository ,
                          BankingService bankingService,
                          PasswordEncoder passwordEncoder
    ){
        this.accountRepository = accountRepository;
        this.bankingService = bankingService;
        this.passwordEncoder = passwordEncoder;
    }

    public Account createAccount(Account account ){

        account.setPinHash(passwordEncoder.encode(account.getPinHash()));

        account.setBalance(new BigDecimal("0.0"));

        return accountRepository.save(account);
    }

    public void updateBalance(Account account , BigDecimal newBalance){

        account.setBalance(newBalance);
        accountRepository.save(account);
    }

    public Account findAccount(Long accNo){
        return accountRepository.findById(accNo)
                .orElseThrow( () -> new RuntimeException("Account not found"));
    }

    public boolean hasActiveAccount(Long customerId){

        return accountRepository.existsByCustomerCustomerId(customerId);

    }

}
