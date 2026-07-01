package com.bank.unitedbank.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawDTO {

    @NotNull(message = "account id invalid")
    private Long accNo;

    @NotBlank(message = "pin blank")
    @Pattern(regexp = "^[0-9]{4}$" , message = "not 4 digits")
    private String pin;

    @NotNull(message = "amount invalid")
    @Min(value = 1 , message = "amount invalid")
    private BigDecimal amount;

}
