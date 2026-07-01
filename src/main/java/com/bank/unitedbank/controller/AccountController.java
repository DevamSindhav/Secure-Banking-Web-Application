package com.bank.unitedbank.controller;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.unitedbank.service.CustomerService;
import com.bank.unitedbank.dto.request.NewAccountDTO;
import com.bank.unitedbank.entity.Account;
import com.bank.unitedbank.mapper.CreateRequestMapper;

@RestController
@RequestMapping("/create")
public class AccountController {

    private final CustomerService customerService;

    public AccountController(CustomerService customerService){
        this.customerService = customerService;
    }

    @PostMapping("/account")
    public ResponseEntity<?> createNewAccount(@Valid @RequestBody NewAccountDTO newAccountDTO){

        Long customerId = (Long) SecurityContextHolder.getContext()
                                    .getAuthentication().getPrincipal();

        Account account =
                CreateRequestMapper.toAccountEntity(newAccountDTO);

        customerService.createAccount(customerId , newAccountDTO.getPassword() , account);

        return new ResponseEntity<>("Account created successfully!" , HttpStatus.CREATED);
    }

}
