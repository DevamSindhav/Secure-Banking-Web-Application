package com.bank.unitedbank.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositDTO {

    @NotNull(message = "account number invalid")
    private Long accNo;

    @NotNull(message = "amount invalid")
    @Min(value = 1 , message = "amount invalid")
    private BigDecimal amount;

}
