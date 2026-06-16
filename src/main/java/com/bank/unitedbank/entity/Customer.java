package com.bank.unitedbank.entity;

//lombok that handles all getters and setters
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
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;



@Entity //hibernate annotation tells this is a database entity
@Table(name = "customers_data")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SQLRestriction("is_customer_valid = true")
public class Customer{

	@Id //tells that this is primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) //for auto accNO generation
	@Column(name = "customer_id")
	private Long customerId;

	@Column(name = "fullName" , nullable = false)
	private String fullName;

	@Column(name = "email" , nullable = false)
	private String email;

	@Column(name = "password_hashed" , nullable = false)
	private String passwordHashed;

	//this tells that customer can have multiple Accounts
	//no cascade Type chosen as we don't want to delete customer
	//till all accounts are active
	@OneToMany(mappedBy = "customer")
	List<Account> accountList;

	@Column(name = "address" , nullable = false)
	private String address;

	@Column(name = "postal_code" , nullable = false)
	private String postalCode;

	@Column(name = "mobile_number")
	private String mobileNo;

	@Column(name = "date_of_birth" , nullable = false)
	private LocalDate dob;

	@Column(name = "registered_at" , updatable = false , nullable = false)
	@CreationTimestamp
	private Instant registeredAt;

	//Decided to implement SoftDelete rather than a permanent delete
	//as I read that this is the bank standard
	//same is for account and transaction
	@Column(name = "is_customer_valid" , nullable = false)
	private Boolean isCustomerValid = true;

}