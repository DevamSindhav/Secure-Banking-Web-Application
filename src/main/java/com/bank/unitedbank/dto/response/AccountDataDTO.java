package com.bank.unitedbank.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDataDTO {

    private Long accNo;
    private String accType;
    private BigDecimal balance;

}
