package com.bank.unitedbank.mapper;

import com.bank.unitedbank.entity.Customer;
import com.bank.unitedbank.dto.response.CustomerDataDTO;

import com.bank.unitedbank.entity.Account;
import com.bank.unitedbank.dto.response.AccountDataDTO;

public class DataResponseMapper {

    public static CustomerDataDTO toCustomerResponseDTO(Customer customer){
        return new CustomerDataDTO(
            customer.getCustomerId(),
            customer.getFullName(),
            customer.getEmail(),
            customer.getAddress(),
            customer.getMobileNo(),
            customer.getPostalCode(),
            customer.getDob(),
            customer.getAccountList().stream().
                    map(Account::getAccNo).toList()
        );
    }

    public static AccountDataDTO toAccountDataDTO(Account account){

        return new AccountDataDTO(
                account.getAccNo(),
                account.getAccType(),
                account.getBalance()
        );
    }

}
