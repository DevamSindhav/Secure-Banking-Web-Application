package com.bank.unitedbank.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDataDTO {

    //For only account details
    public AccountDataDTO(Long accNo , String accType , BigDecimal balance){
        this.accNo = accNo;
        this.accType = accType;
        this.balance = balance;
    }

    //Full args constructor for account and transaction details

    private Long accNo;
    private String accType;
    private BigDecimal balance;
    private List<TransactionDataDTO> transactionDataDTOList;

}
