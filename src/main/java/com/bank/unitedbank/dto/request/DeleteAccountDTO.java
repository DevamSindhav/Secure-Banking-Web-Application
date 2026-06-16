package com.bank.unitedbank.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteAccountDTO {

    @NotNull(message = "customer id invalid")
    private Long customerId;

    @NotNull(message = "account number invalid")
    private Long accNo;

    @NotBlank(message = "password blank")
    @Size(min = 8 , max = 16 , message = "password not of length range 8-16")
    private String password;

    @NotBlank(message = "pin blank")
    @Pattern(regexp = "^[0-9]{4}$" , message = "not 4 digits")
    private String newPin;

}
