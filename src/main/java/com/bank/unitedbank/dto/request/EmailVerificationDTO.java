package com.bank.unitedbank.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailVerificationDTO {

    public EmailVerificationDTO(String email){
        this.email = email;
    }

    @NotBlank(message = "Email can not be Blank!")
    @Email(message = "Invalid Email!")
    private String email;

    @Range(min = 1000 , max = 9999 , message = "Otp must be 4 digits")
    private Integer otp;

}
