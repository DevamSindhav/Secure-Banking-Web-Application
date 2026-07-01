package com.bank.unitedbank.entity;


import jakarta.persistence.Column;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "otp_verification")
public class OtpEntity {

    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "otp" , nullable = false)
    private Integer otp;

    @Column(name = "time_stamp" , nullable = false)
    private Instant timeStamp;

}
