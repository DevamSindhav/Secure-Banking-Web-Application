package com.bank.Unitedbank.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.bank.Unitedbank.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDTO{

    private Integer accNo;
    private String fullName;
    private String email;
    private String accType;
    private String mobileNo;
    private String address;
    private String postalCode;
    private LocalDate dob;
    private BigDecimal balance;

    //Methods to convert the Customer to CustomerResponse
    public static CustomerResponseDTO convertCustomerToCRDTO(Customer customer) {
        return new CustomerResponseDTO(
                customer.getAccNo(),
                customer.getFullName(),
                customer.getEmail(),
                customer.getAccType(),
                customer.getMobileNo(),
                customer.getAddress(),
                customer.getPostalCode(),
                customer.getDob(),
                customer.getBalance()
        );
    }

}

