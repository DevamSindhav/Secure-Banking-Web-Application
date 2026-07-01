package com.bank.unitedbank.dto.request;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewAccountDTO {

    @NotBlank(message = "password blank")
    @Size(min = 8 , max = 16 , message = "password not of length range 8-16")
    private String password;

    @NotBlank(message = "account type blank")
    @Pattern(regexp = "^(Savings|Current)$")
    private String accType;

    @NotNull(message = "amount invalid")
    @Min(value = 1000 , message = "initial balance less than 1000")
    private BigDecimal balance;

    @NotBlank(message = "pin blank")
    @Pattern(regexp = "^[0-9]{4}$" , message = "not 4 digits")
    private String pin;


}
