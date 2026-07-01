package com.bank.unitedbank.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginDTO {

    public LoginDTO(){}

    @Email(message = "email invalid")
    @NotBlank(message = "email blank")
    private String email;

    @NotBlank(message = "password blank")
    @Size(min = 8 , max = 16 , message = "password not of length range 8-16")
    private String password;

}
