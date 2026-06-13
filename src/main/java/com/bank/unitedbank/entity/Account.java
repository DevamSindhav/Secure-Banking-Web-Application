package com.bank.unitedbank.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "account_data")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SQLRestriction("is_account_valid = true")
public class Account {

    @Id//tells that this is primary key
    @Column(name = "account_number")
    @GeneratedValue(strategy = GenerationType.IDENTITY)//for auto accNO generation
    private Long accNo;

    @ManyToOne
    @JoinColumn(name = "customer_id" , nullable = false)
    private Customer customer;

    @Column(name = "account_type", nullable = false)
    private String accType;

    @Column(name = "balance" , nullable = false)
    private BigDecimal balance;

    @Column(name = "pin_hashed" , nullable = false)
    private String pinHash;

    @Column(name = "created_at" , nullable = false , updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    //for soft delete implementation
    @Column(name = "is_account_valid" , nullable = false)
    private Boolean isAccountValid = true;

    //Mapping Transactions to account
    @OneToMany(mappedBy = "account")
    List<Transaction> transactionList;

}
