package com.bank.unitedbank.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountPinDTO {

    @NotNull(message = "customer id invalid")
    private Long customerId;

    @NotNull(message = "account id invalid")
    private Long accNo;

    @NotBlank(message = "password blank")
    @Size(min = 8 , max = 16 , message = "old password not of length range 8-16")
    private String password;

    @NotBlank(message = "pin blank")
    @Pattern(regexp = "^[0-9]{4}$" , message = "not 4 digits")
    private String newPin;

}
