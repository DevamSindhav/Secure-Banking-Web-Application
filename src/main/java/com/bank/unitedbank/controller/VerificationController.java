package com.bank.unitedbank.controller;



import com.bank.unitedbank.dto.request.EmailVerificationDTO;
import com.bank.unitedbank.exception.OtpException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.unitedbank.service.EmailService;
import com.bank.unitedbank.service.OtpService;
import com.bank.unitedbank.service.CustomerService;

@RestController
@RequestMapping("/verify")
public class VerificationController {

    private final CustomerService customerService;
    private final EmailService emailService;
    private final OtpService otpService;

    public VerificationController(
            CustomerService customerService,
            EmailService emailService,
            OtpService otpService
    ){
        this.customerService = customerService;
        this.emailService = emailService;
        this.otpService = otpService;
    }

    @PostMapping("/email")
    public ResponseEntity<?> sendOtp(@Valid @RequestBody EmailVerificationDTO emailVerificationDTO){

        if(customerService.isEmailVerified(emailVerificationDTO.getEmail())){
            return new ResponseEntity<>("Email is verified." , HttpStatus.CONTINUE);
        }

        emailService.emailVerificationEmail(emailVerificationDTO.getEmail());

        return new ResponseEntity<>("OTP sent successfully on Email" , HttpStatus.OK);
    }

    @PostMapping("/otp")
    public ResponseEntity<?> verifyEmail(@Valid @RequestBody EmailVerificationDTO emailVerificationDTO){
        //tried to make it work without database read
        if(emailVerificationDTO.getOtp() == null){
            throw new OtpException("Fill the OTP!");
        }

        boolean isOtpValid =
                otpService.isOtpValid(
                        emailVerificationDTO.getEmail(),
                        emailVerificationDTO.getOtp()
                );

        if(!isOtpValid){
            throw new OtpException("Wrong OTP!");
        }

        customerService.markEmailVerified(emailVerificationDTO.getEmail());

        otpService.deleteOtp(emailVerificationDTO.getEmail());

        return new ResponseEntity<>("Email verified!" , HttpStatus.OK);
    }

}
