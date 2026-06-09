/*
code can be shorter if I don't initialize temporary variables,
but I have decided to keep those vars for the readability
*/

package com.bank.Unitedbank.service;


import jakarta.transaction.Transactional;
import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bank.Unitedbank.entity.Customer;
import com.bank.Unitedbank.entity.Transaction;
import com.bank.Unitedbank.repository.CustomerRepository;
import com.bank.Unitedbank.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class TransactionService {
	
	private final CustomerRepository customerRepository;
	private final TransactionRepository transactionRepository;
	private final PasswordEncoder passwordEncoder;

	public TransactionService(CustomerRepository cRepository , TransactionRepository tRepository ,
									PasswordEncoder pEncoder) {
		this.customerRepository = cRepository;
		this.passwordEncoder = pEncoder;
		this.transactionRepository = tRepository;
	}
	
	
	public void deposit(Integer accNo , BigDecimal amount) {
		
		Customer customer = customerRepository.findById(accNo)
							.orElseThrow(() -> new RuntimeException("Account not found"));
		

		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
	        throw new RuntimeException("Deposit amount must be greater than zero.");
	    }
		
		BigDecimal currentBalance = customer.getBalance();
		
		customer.setBalance(currentBalance.add(amount));
		
		Transaction transaction = new Transaction(customer , amount , "DEPOSIT");
		
		customerRepository.save(customer);
		transactionRepository.save(transaction);
		
	}
	
	
	public void withdraw(Integer accNo , String plainPin , BigDecimal amount) {
		
		Customer customer = customerRepository.findById(accNo)
				.orElseThrow(() -> new RuntimeException("Account not found."));
		
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
	        throw new RuntimeException("Withdrawal amount must be greater than zero.");
	    }
		
		boolean isMatch = passwordEncoder.matches(plainPin , customer.getPin());
		
		if(!isMatch) {
			throw new RuntimeException("Pin is incorrect.");
		}
		
		BigDecimal currentBalance = customer.getBalance();
		if(currentBalance.compareTo(amount) < 0) {
			
			throw new RuntimeException("Not enough balance.");
		}
		
		BigDecimal newBalance = currentBalance.subtract(amount);
		
		customer.setBalance(newBalance);
		
		Transaction transaction = new Transaction(customer, amount , "WITHDRAW");
		
		customerRepository.save(customer);
		transactionRepository.save(transaction);
		
	}
	
	public void transfer(Integer senderAccNo ,String plainPin , Integer receiverAccNo , BigDecimal amount) {
		
		Customer sender = customerRepository.findById(senderAccNo)
				.orElseThrow(() -> new RuntimeException("Account not found."));
		
		Customer receiver = customerRepository.findById(receiverAccNo)
				.orElseThrow(() -> new RuntimeException("Receiver not found."));
		
		if (senderAccNo.equals(receiverAccNo)) {
	        throw new RuntimeException("Cannot transfer money to the same account.");
	    }
		
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
	        throw new RuntimeException("Withdrawal amount must be greater than zero.");
	    }
		
		boolean isMatch = passwordEncoder.matches(plainPin , sender.getPin() );
		
		if(!isMatch) {
			throw new RuntimeException("Pin is incorrect.");
		}
		
		BigDecimal senderCurrentBalance = sender.getBalance();
		BigDecimal receiverCurrentBalance = receiver.getBalance();
		
		if(senderCurrentBalance.compareTo(amount) < 0) {
			throw new RuntimeException("Not enough balance.");
		}
		
		BigDecimal senderNewBalance = senderCurrentBalance.subtract(amount);
		BigDecimal receiverNewBalance = receiverCurrentBalance.add(amount);
		
		sender.setBalance(senderNewBalance);
		receiver.setBalance(receiverNewBalance);
		
		Transaction senderTransaction = new Transaction(sender , amount , "WITHDRAW");
		Transaction receiverTransaction = new Transaction(receiver , amount , "DEPOSIT");
		
		customerRepository.save(sender);
		customerRepository.save(receiver);
		transactionRepository.save(senderTransaction);
		transactionRepository.save(receiverTransaction);
		
	}
	
	public List<Transaction> getAllStatement(Integer accNo){
		
		return transactionRepository.findByCustomerAccNoOrderByTimeStampDesc(accNo);
	}
	
	public List<Transaction> getMiniStatement(Integer accNo){
		
		return transactionRepository.findTop10ByCustomerAccNoOrderByTimeStampDesc(accNo);
	}

	public List<Transaction> getMonthStatement(Integer accNo, LocalDateTime startDate , LocalDateTime endDate ){

		return transactionRepository.findByCustomerAccNoAndTimeStampBetweenOrderByTimeStampDesc(accNo , startDate , endDate);
	}
	
}
