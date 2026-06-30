package com.bank.unitedbank.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordDTO {

    public ForgotPasswordDTO(String email){
        this.email = email;
    }

    public ForgotPasswordDTO(String email , Integer otp){
        this.email = email;
        this.otp = otp;
    }

    @Email(message = "Invalid email!")
    @NotBlank(message = "Email can not be blank!")
    private String email;

    @Range(min = 1000 , max = 9999 , message = "Otp must be 4 digits")
    private Integer otp;


    @Size(min = 8 , max = 16 , message = "password not of length range 8-16")
    private String newPassword;
}
