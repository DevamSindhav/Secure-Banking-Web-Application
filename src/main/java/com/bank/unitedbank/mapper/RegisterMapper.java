package com.bank.unitedbank.mapper;

import com.bank.unitedbank.entity.Customer;
import com.bank.unitedbank.entity.Account;
import com.bank.unitedbank.dto.request.RegisterDTO;

public class RegisterMapper {

    public static Customer toCustomerEntity(RegisterDTO registerDTO){

        return Customer.builder()
                .fullName(registerDTO.getFullName())
                .email(registerDTO.getEmail())
                .passwordHashed(registerDTO.getPassword())
                .address(registerDTO.getAddress())
                .postalCode(registerDTO.getPostalCode())
                .mobileNo(registerDTO.getMobileNo())
                .dob(registerDTO.getDob())
                .build();
    }

    public static Account toAccountEntity(RegisterDTO registerDTO){

        return Account.builder()
                .accType(registerDTO.getAccType())
                .balance(registerDTO.getBalance())
                .pinHashed(registerDTO.getPin())
                .build();

    }

}
