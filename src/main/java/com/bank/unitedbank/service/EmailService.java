package com.bank.unitedbank.service;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final OtpService otpService;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(
            JavaMailSender mailSender ,
            OtpService otpService
    ){
        this.mailSender = mailSender;
        this.otpService = otpService;
    }

    public void emailVerificationEmail(String toEmail){
        SimpleMailMessage message = new SimpleMailMessage();

        Integer otp = otpService.generateNewOtp(toEmail);

        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Email verification OTP");
        message.setText("The OTP for the email verification is " + otp + ".\nplease do not share this with anyone!");

        mailSender.send(message);
    }

    public void passResetEmail(String toEmail){

        SimpleMailMessage message = new SimpleMailMessage();

        Integer otp = otpService.generateNewOtp(toEmail);

        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Password change OTP");
        message.setText("The OTP for password change is " + otp + ".\nplease do not share this with anyone!");

        mailSender.send(message);

    }


}
