package com.bank.unitedbank.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bank.unitedbank.entity.Customer;
import com.bank.unitedbank.entity.Account;
import com.bank.unitedbank.repository.CustomerRepository;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;

@Service
//transactional annotation helps to make sure that it is done then it is done
//100 percent, or it does not change anything i.e. rollback
@Transactional
public class CustomerService {
	
	private final CustomerRepository customerRepository;
	private final PasswordEncoder passwordEncoder;
	private final AccountService accountService;
	private final TransactionService transactionService;
	private final BankingService bankingService;
	
	public  CustomerService(CustomerRepository customerRepo ,
							PasswordEncoder passEncode ,
							AccountService accountService,
							TransactionService transactionService,
							BankingService bankingService
	) {
		
		this.customerRepository = customerRepo;
		this.passwordEncoder = passEncode;
		this.accountService = accountService;
		this.transactionService = transactionService;
		this.bankingService = bankingService;
	}
	
	public Customer registerCustomer(Customer customer , Account account) {
		
		
		//email already exists or not is checked
		if(customerRepository.findByEmail(customer.getEmail()).isPresent()) {
			throw new RuntimeException("This email is already Registered.");
		}
		BigDecimal initialBalance = account.getBalance();
		customer.setPassword(passwordEncoder.encode(customer.getPassword()));

		Customer registeredCustomer = customerRepository.save(customer);

		account.setCustomer(registeredCustomer);
		Account initialAccount = accountService.createAccount(account);
		bankingService.deposit(initialAccount.getAccNo() , initialBalance);

		return registeredCustomer;
	}
	
	public Customer getCustomerById(Long customerId) {
		
		return customerRepository.findById(customerId)
				.orElseThrow(() -> new RuntimeException("Customer not found."));
	}
	
	public Customer loginValidation(String email , String plainPass) {
		
		Customer customer = customerRepository.findByEmail(email)
							.orElseThrow(() -> new RuntimeException("Customer not found."));
		
		boolean isMatch = passwordEncoder.matches(plainPass , customer.getPassword() );
		
		if(!isMatch) {
			throw new RuntimeException("Password is incorrect.");
		}
		
		return customer;
	}
	
	
	public void passwordUpdate(Long customerId , String oldPlainPass , String newPlainPass) {
		
		Customer customer = customerRepository.findById(customerId)
							.orElseThrow(() -> new RuntimeException("Customer not found."));
		
		if(!passwordEncoder.matches(oldPlainPass, customer.getPassword())) {
			throw new RuntimeException("Password is incorrect");
		}

		customer.setPassword(passwordEncoder.encode(newPlainPass));
		
		customerRepository.save(customer);
		
	}
	
//	public void pinUpdate(Integer accNo , String plainPass , String newPlainPin) {
//
//		Customer customer = customerRepository.findById(accNo)
//				.orElseThrow(() -> new RuntimeException("Account not found."));
//
//
//		if(!passwordEncoder.matches(plainPass, customer.getPassword())) {
//			throw new RuntimeException("Password is incorrect");
//		}
//
//
//		customer.setPin(passwordEncoder.encode(newPlainPin));
//
//		customerRepository.save(customer);
//
//	}
//
	public void deleteAccount(Long customerId , String plainPass) {

		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new RuntimeException("Customer not found."));

		if(!passwordEncoder.matches(plainPass, customer.getPassword())) {
			throw new RuntimeException("Password is incorrect");
		}

		//Don't let customer delete his id before deactivating the all accounts
		boolean hasActiveAccount = accountService.hasActiveAccount(customerId);
		if(hasActiveAccount){
			throw new RuntimeException("Please Deactivate all Accounts");
		}

		customer.setIsCustomerValid(false);
		customerRepository.save(customer);
	}

}
