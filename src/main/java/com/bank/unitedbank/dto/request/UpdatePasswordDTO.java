package com.bank.unitedbank.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordDTO {

    @NotNull(message = "customer id invalid")
    private Long customerId;

    @NotBlank(message = "old password blank")
    @Size(min = 8 , max = 16 , message = "old password not of length range 8-16")
    private String oldPassword;

    @NotBlank(message = "new password blank")
    @Size(min = 8 , max = 16 , message = "new password not of length range 8-16")
    private String newPassword;

}
