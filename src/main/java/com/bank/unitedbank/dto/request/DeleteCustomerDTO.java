package com.bank.unitedbank.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteCustomerDTO {

    @NotNull(message = "customer id invalid")
    private Long customerId;

    @NotBlank(message = "password blank")
    @Size(min = 8 , max = 16 , message = "password not of length range 8-16")
    private String password;

}
