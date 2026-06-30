package com.bank.unitedbank.service;

import com.bank.unitedbank.exception.OtpException;
import com.bank.unitedbank.repository.OtpRepository;
import com.bank.unitedbank.entity.OtpEntity;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Transactional
public class OtpService {

    private final OtpRepository otpRepository;

    public OtpService(OtpRepository otpRepository){
        this.otpRepository = otpRepository;
    }


    public Integer generateNewOtp(String email){
       Integer otp = (int) (java.lang.Math.random() * (9999-1000 + 1));

       //overwrite the otp if we have the same instant
       if(otpRepository.existsById(email)){
           OtpEntity otpEntity = otpRepository.getReferenceById(email);
           otpEntity.setOtp(otp);
           otpEntity.setTimeStamp(Instant.now().plusSeconds(60));
           otpRepository.save(otpEntity);
           return otp;
       }

        OtpEntity otpEntity = new OtpEntity(
                email,
                otp,
                Instant.now().plusSeconds(60)
        );

        otpRepository.save(otpEntity);

        return otp;
    }

    public boolean isOtpValid(String email , Integer otp){

        OtpEntity otpEntity = otpRepository.findById(email)
                .orElseThrow( () -> new OtpException("Otp not found! Please click Resend Otp!"));

        boolean isAfterExpTime =
                Instant.now().isAfter(otpEntity.getTimeStamp());


        return !isAfterExpTime && otp.equals(otpEntity.getOtp());
    }

    public void deleteOtp(String email){

      otpRepository.deleteById(email);

    }

}
