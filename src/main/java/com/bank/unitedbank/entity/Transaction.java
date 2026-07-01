package com.bank.unitedbank.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import java.math.BigDecimal;
import java.time.Instant;


@Entity
@Table(name = "transaction_records")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction{

	public Transaction(Account account , BigDecimal amount , String transactionType) {

		this.account = account;
		this.amount = amount;
		this.transactionType = transactionType;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_id")
	private Long transactionId;


	@ManyToOne
	@JoinColumn(name = "account_number" , nullable = false)
	private Account account;
/*
 * beacuse we have mapped them we no longer need this field hibernate will take care
 * to store the appropriate column data in the table
	@Column(name = "account_number")
	private int accNo;
*/

	@Column(name = "amount" , nullable = false)
	private BigDecimal amount;

	@Column(name = "transaction_type" , nullable = false)
	private String transactionType;

	@Column(name = "transaction_time" , updatable = false)
	@CreationTimestamp
	private Instant timeStamp;

}