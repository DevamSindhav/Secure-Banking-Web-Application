package com.bank.unitedbank.service;

import com.bank.unitedbank.exception.*;
import org.springframework.stereotype.Service;

import com.bank.unitedbank.entity.Customer;
import com.bank.unitedbank.entity.Account;
import com.bank.unitedbank.repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Service
//transactional annotation helps to make sure that it is done then it is done
//100 percent, or it does not change anything i.e. rollback
@Transactional
public class CustomerService {
	
	private final CustomerRepository customerRepository;
	private final PasswordEncoder passwordEncoder;
	private final AccountService accountService;
	private final BankingService bankingService;
	
	public  CustomerService(CustomerRepository customerRepo ,
							PasswordEncoder passEncode ,
							AccountService accountService,
							BankingService bankingService
	){
		this.customerRepository = customerRepo;
		this.passwordEncoder = passEncode;
		this.accountService = accountService;
		this.bankingService = bankingService;
	}
	
	public Customer registerCustomer(Customer customer , Account account) {

		//email already exists or not is checked
		if(customerRepository.findByEmail(customer.getEmail()).isPresent()) {
			throw new CustomerExistsException("This email is already Registered.");
		}
		//Age checking logic
		int age = Period.between(customer.getDob() , LocalDate.now()).getYears();

		if(age < 18 || age > 100){
			throw new AgeInvalidException("Age is not between 18 and 100.");
		}

		BigDecimal initialBalance = account.getBalance();

		BigDecimal minIniBalance = new BigDecimal("1000.0");

		if(initialBalance.compareTo(minIniBalance) < 0){
			throw new IniBalanceInvalidException("Initial Balance is Less Than minimum Balance.");
		}

		customer.setPasswordHashed(passwordEncoder.encode(customer.getPasswordHashed()));

		Customer registeredCustomer = customerRepository.save(customer);

		account.setCustomer(registeredCustomer);
		Account initialAccount = accountService.createAccount(account);
		bankingService.deposit(initialAccount.getAccNo() , initialBalance);

		return registeredCustomer;
	}

	public Customer loginValidation(String email , String plainPass) {
		
		Customer customer = getCustomerByEmail(email);

		if(!isPasswordCorrect(customer , plainPass)) {
			throw new PasswordIncorrectException("Password is incorrect.");
		}
		
		return customer;
	}
	
	
	public void passwordUpdate(Long customerId , String oldPlainPass , String newPlainPass) {
		
		Customer customer = getCustomerById(customerId);
		
		if(!isPasswordCorrect(customer , oldPlainPass)) {
			throw new PasswordIncorrectException("Password is incorrect");
		}
		if(passwordEncoder.matches(newPlainPass , customer.getPasswordHashed())){
			throw new SameNewPasswordException("new password can not be same as the old password");
		}

		customer.setPasswordHashed(passwordEncoder.encode(newPlainPass));
		
		customerRepository.save(customer);
		
	}

	public void deleteCustomer(Long customerId , String plainPass) {

		Customer customer = getCustomerById(customerId);

		if(!isPasswordCorrect(customer , plainPass)) {
			throw new PasswordIncorrectException("Password is incorrect");
		}

		//Don't let customer delete his id before deactivating the all accounts
		boolean hasActiveAccount = accountService.hasActiveAccount(customerId);
		if(hasActiveAccount){
			throw new ActiveAccountException("Please Deactivate all Accounts");
		}

		customer.setIsCustomerValid(false);

		customerRepository.save(customer);
	}

	public void deleteAccount(Long customerId , Long accNo , String plainPass){
		Customer customer = getCustomerById(customerId);

		if(!isPasswordCorrect(customer, plainPass)){
			throw new PasswordIncorrectException("Password is incorrect");
		}

		if(!isOwnerOfAccount(customer , accNo)){
			throw new UnauthorizedAccountException("Account not found");
		}

		accountService.deleteAccount(accNo);
	}

	public void updateAccountPin(Long customerId , Long accNo , String plainPass , String newPlainPin){
		Customer customer = getCustomerById(customerId);

		if(!isPasswordCorrect(customer , plainPass)){
			throw new PasswordIncorrectException("Password is incorrect");
		}

		if(!isOwnerOfAccount(customer , accNo)){
			throw new UnauthorizedAccountException("Account not found");
		}

		accountService.updatePin(accNo , newPlainPin);
	}
	//Helper Methods
	public boolean isPasswordCorrect(Customer customer , String plainPass){

		return passwordEncoder.matches(
				plainPass,
				customer.getPasswordHashed()
		);
	}

	public Customer getCustomerById(Long customerId) {

		return customerRepository.findById(customerId)
				.orElseThrow(() -> new CustomerNotFoundException("Customer not found."));
	}

	public Customer getCustomerByEmail(String email){
		return customerRepository.findByEmail(email)
				.orElseThrow( () -> new CustomerNotFoundException("Customer not found."));
	}

	public boolean isOwnerOfAccount(Customer customer , Long accNo){
		Account account = accountService.getAccountById(accNo);

		return account.getCustomer()
				.getCustomerId()
				.equals(customer.getCustomerId());
	}

}
