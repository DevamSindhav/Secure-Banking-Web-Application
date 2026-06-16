package com.bank.unitedbank.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO{

    @NotBlank(message = "name blank")
    @Size(min = 4 , max = 20 , message = "username not og length range 4-20")
    private String fullName;

    @Email(message = "email invalid")
    @NotBlank(message = "email blank")
    private String email;

    @NotBlank(message = "password blank")
    @Size(min = 8 , max = 16 , message = "password not of length range 8-16")
    private String password;

    @NotBlank(message = "pin blank")
    @Pattern(regexp = "^[0-9]{4}$" , message = "not 4 digits")
    private String pin;

    @NotBlank(message = "account type blank")
    @Pattern(regexp = "^(Savings|Current)$")
    private String accType;

    @NotBlank(message = "account number blank")
    @Pattern(regexp = "^[0-9]{10}$" , message = "mobile number invalid")
    private String mobileNo;

    @NotBlank(message = "address blank")
    @Size(max = 100 , message = "address field max length is 100")
    private String address;

    @NotBlank(message = "postal code blank")
    @Pattern(regexp = "^[0-9]{6}$" , message = "postal code invalid")
    private String postalCode;

    @NotNull(message = "DOB invalid")
    @Past(message = "dob invalid")
    private LocalDate dob;

    @NotNull(message = "amount invalid")
    @Min(value = 1000 , message = "initial balance less than 1000")
    private BigDecimal balance;

}