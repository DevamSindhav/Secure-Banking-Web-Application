package com.bank.unitedbank.service;

import java.math.BigDecimal;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bank.unitedbank.entity.Customer;
import com.bank.unitedbank.repository.CustomerRepository;

import jakarta.transaction.Transactional;

@Service

//transactinal annotaion helps to make sure that it is done then it is done 
//100 percent or it does not change anything i.e rollback
@Transactional
public class CustomerService {
	
	private final CustomerRepository customerRepository;
	private final PasswordEncoder passwordEncoder;
	private final TransactionService transService;
	
	public  CustomerService(CustomerRepository customerRepo , PasswordEncoder passEncode , TransactionService transService) {
		
		this.customerRepository = customerRepo;
		this.passwordEncoder = passEncode;
		this.transService = transService;
	}
	
	public Customer registerCustomer(Customer customer) {
		
		
		//email already exists or not is checked
		if(customerRepository.findByEmail(customer.getEmail()).isPresent()) {
				
			throw new RuntimeException("This email is already Registered.");
		}
			
		BigDecimal initialBalance = customer.getBalance();
		//Filter for minimum first deposit
		if(initialBalance.compareTo(new BigDecimal("1000.0")) == -1) {
					
			throw new RuntimeException("MinimumDeposit of 1000.0 is required to Open an account.");
	
		}
			
		//first we force it to be 0
		//and then we deposit the extracted initialDeposit using the  transactionService
		customer.setBalance(new BigDecimal("0.0"));
			
		customer.setPassword(passwordEncoder.encode(customer.getPassword()));
			
		customer.setPin(passwordEncoder.encode(customer.getPin()));
			
		Customer savedCustomer = customerRepository.save(customer);
			
		transService.deposit(savedCustomer.getAccNo() , initialBalance);
			

		return savedCustomer;
	}
	
	public Customer getCustomerById(Integer accNo) {
		
		return customerRepository.findById(accNo)
				.orElseThrow(() -> new RuntimeException("Account not found."));
	}
	
	public Customer loginValidation(String email , String plainPass) {
		
		Customer customer = customerRepository.findByEmail(email)
							.orElseThrow(() -> new RuntimeException("Account not found."));
		
		boolean isMatch = passwordEncoder.matches(plainPass , customer.getPassword() );
		
		if(!isMatch) {
			throw new RuntimeException("Password is incorrect.");
		}
		
		return customer;
	}
	
	
	public void passwordUpdate(Integer accNo , String oldPlainPass , String newPlainPass) {
		
		Customer customer = customerRepository.findById(accNo)
							.orElseThrow(() -> new RuntimeException("Account not found."));
		
		if(!passwordEncoder.matches(oldPlainPass, customer.getPassword())) {
			throw new RuntimeException("Password is incorrect");
		}
		
		
		customer.setPassword(passwordEncoder.encode(newPlainPass));
		
		customerRepository.save(customer);
		
	}
	
	public void pinUpdate(Integer accNo , String plainPass , String newPlainPin) {
		
		Customer customer = customerRepository.findById(accNo)
				.orElseThrow(() -> new RuntimeException("Account not found."));
		
		
		if(!passwordEncoder.matches(plainPass, customer.getPassword())) {
			throw new RuntimeException("Password is incorrect");
		}
		
		
		customer.setPin(passwordEncoder.encode(newPlainPin));
		
		customerRepository.save(customer);
		
	}
	
	public void deleteAccount(Integer accNo , String plainPass) {
		
		Customer customer = customerRepository.findById(accNo)
				.orElseThrow(() -> new RuntimeException("Account not found."));
		
		if(!passwordEncoder.matches(plainPass, customer.getPassword())) {
			throw new RuntimeException("Password is incorrect");
		}
		
		customerRepository.deleteById(accNo);
	}

}
