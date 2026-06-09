package com.bank.Unitedbank.entity;


//lombok that handels all getters and setters
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;

//hibernate imports needed
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;



@Entity //hibernate annotation tells this is a database entity
@Table(name = "customers_data")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer{
	
	//need a custom constructor for transition between
	//DTO and a real entity

	public Customer(
		String fullName,
		String email,
		String password,
		BigDecimal balance,
		String accType,
		String mobileNo,
		String address,
		String pin,
		String postalCode,
		LocalDate dob
	){
		this.fullName = fullName;
		this.email = email;
		this.password = password;
		this.balance = balance;
		this.accType = accType;
		this.mobileNo = mobileNo;
		this.address = address;
		this.pin = pin;
		this.postalCode = postalCode;
		this.dob = dob;
	}


	@Id //tells that this is primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) //for auto accNO generation
	@Column(name = "account_number")
	private Integer accNo;
	
	@Column(name = "fullName")
	private String fullName;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "password_hashed")
	private String password;
	
	@Column(name = "balance")
	private BigDecimal balance;
	
	@Column(name = "account_type")
	private String accType;
	
	@Column(name = "mobile_no")
	private String mobileNo;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "pin_hashed")
	private String pin;
	
	@Column(name = "postal_code")
	private String postalCode;
	
	@Column(name = "date_of_birth")
	private LocalDate dob;
	
	@Column(name = "created_at" , updatable = false)
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	
	//this tell that a customer can have multiple transaction not other way around
	@OneToMany(mappedBy = "customer" , cascade = CascadeType.ALL)
	List<Transaction> transactions;


}