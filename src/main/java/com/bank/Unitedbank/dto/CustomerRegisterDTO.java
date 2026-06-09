package com.bank.Unitedbank.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.bank.Unitedbank.entity.Customer;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRegisterDTO{

    private String fullName;
    private String email;
    private String password;
    private String pin;
    private String accType;
    private String mobileNo;
    private String address;
    private String postalCode;
    private LocalDate dob;
    private BigDecimal balance;

    //Method to convert CustomerRegister to Customer
    public static Customer convertCRegDTOtoCustomer(CustomerRegisterDTO customerRegisterDTO){
        return new Customer(
                customerRegisterDTO.getFullName(),
                customerRegisterDTO.getEmail(),
                customerRegisterDTO.getPassword(),
                customerRegisterDTO.getBalance(),
                customerRegisterDTO.getAccType(),
                customerRegisterDTO.getMobileNo(),
                customerRegisterDTO.getAddress(),
                customerRegisterDTO.getPin(),
                customerRegisterDTO.getPostalCode(),
                customerRegisterDTO.getDob()
        );
    }

}